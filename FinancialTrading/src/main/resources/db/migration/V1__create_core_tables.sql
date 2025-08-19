-- Real-Time Financial Trading System Database Schema
-- High-performance schema optimized for trading operations

-- Enable UUID extension for better ID generation
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Users table with enhanced security and profile management
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    user_id VARCHAR(50) UNIQUE NOT NULL DEFAULT gen_random_uuid()::text,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    phone_number VARCHAR(20),
    country_code VARCHAR(3),
    kyc_status VARCHAR(20) DEFAULT 'PENDING' CHECK (kyc_status IN ('PENDING', 'VERIFIED', 'REJECTED')),
    account_type VARCHAR(20) DEFAULT 'INDIVIDUAL' CHECK (account_type IN ('INDIVIDUAL', 'INSTITUTIONAL', 'MARKET_MAKER')),
    risk_profile VARCHAR(20) DEFAULT 'CONSERVATIVE' CHECK (risk_profile IN ('CONSERVATIVE', 'MODERATE', 'AGGRESSIVE')),
    two_factor_enabled BOOLEAN DEFAULT FALSE,
    account_status VARCHAR(20) DEFAULT 'ACTIVE' CHECK (account_status IN ('ACTIVE', 'SUSPENDED', 'CLOSED')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_login_at TIMESTAMP,
    failed_login_attempts INTEGER DEFAULT 0,
    locked_until TIMESTAMP
);

-- Instruments/Symbols table for trading pairs
CREATE TABLE instruments (
    id BIGSERIAL PRIMARY KEY,
    symbol VARCHAR(20) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    base_currency VARCHAR(10) NOT NULL,
    quote_currency VARCHAR(10) NOT NULL,
    instrument_type VARCHAR(20) DEFAULT 'SPOT' CHECK (instrument_type IN ('SPOT', 'FUTURES', 'OPTIONS', 'SWAP')),
    tick_size DECIMAL(20,8) NOT NULL DEFAULT 0.01,
    min_quantity DECIMAL(20,8) NOT NULL DEFAULT 0.001,
    max_quantity DECIMAL(20,8),
    price_precision INTEGER DEFAULT 8,
    quantity_precision INTEGER DEFAULT 8,
    trading_hours JSONB, -- Store complex trading schedule as JSON
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Enhanced orders table (your existing structure with optimizations)
CREATE TABLE orders (
    id BIGSERIAL PRIMARY KEY,
    order_id VARCHAR(50) UNIQUE NOT NULL DEFAULT gen_random_uuid()::text,
    user_id VARCHAR(50) NOT NULL,
    symbol VARCHAR(20) NOT NULL,
    side VARCHAR(4) NOT NULL CHECK (side IN ('BUY', 'SELL')),
    type VARCHAR(20) NOT NULL CHECK (type IN ('MARKET', 'LIMIT', 'STOP', 'STOP_LIMIT', 'IOC', 'FOK')),
    quantity DECIMAL(20,8) NOT NULL CHECK (quantity > 0),
    price DECIMAL(20,8),
    stop_price DECIMAL(20,8),
    filled_quantity DECIMAL(20,8) DEFAULT 0 CHECK (filled_quantity >= 0),
    average_price DECIMAL(20,8) DEFAULT 0,
    status VARCHAR(20) DEFAULT 'PENDING' CHECK (status IN ('PENDING', 'OPEN', 'PARTIALLY_FILLED', 'FILLED', 'CANCELLED', 'REJECTED', 'EXPIRED')),
    time_in_force VARCHAR(10) DEFAULT 'GTC' CHECK (time_in_force IN ('GTC', 'IOC', 'FOK', 'DAY')),
    client_order_id VARCHAR(50), -- For client reference
    parent_order_id VARCHAR(50), -- For complex order types
    execution_instructions JSONB, -- Additional execution parameters
    fees_paid DECIMAL(20,8) DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP,
    
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (symbol) REFERENCES instruments(symbol)
);

-- Trades table for executed transactions
CREATE TABLE trades (
    id BIGSERIAL PRIMARY KEY,
    trade_id VARCHAR(50) UNIQUE NOT NULL DEFAULT gen_random_uuid()::text,
    symbol VARCHAR(20) NOT NULL,
    buyer_order_id VARCHAR(50) NOT NULL,
    seller_order_id VARCHAR(50) NOT NULL,
    buyer_user_id VARCHAR(50) NOT NULL,
    seller_user_id VARCHAR(50) NOT NULL,
    quantity DECIMAL(20,8) NOT NULL CHECK (quantity > 0),
    price DECIMAL(20,8) NOT NULL CHECK (price > 0),
    trade_value DECIMAL(20,8) GENERATED ALWAYS AS (quantity * price) STORED,
    buyer_fee DECIMAL(20,8) DEFAULT 0,
    seller_fee DECIMAL(20,8) DEFAULT 0,
    trade_type VARCHAR(20) DEFAULT 'NORMAL' CHECK (trade_type IN ('NORMAL', 'WASH', 'CROSS')),
    settlement_status VARCHAR(20) DEFAULT 'PENDING' CHECK (settlement_status IN ('PENDING', 'SETTLED', 'FAILED')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    settled_at TIMESTAMP,
    
    FOREIGN KEY (buyer_order_id) REFERENCES orders(order_id),
    FOREIGN KEY (seller_order_id) REFERENCES orders(order_id),
    FOREIGN KEY (buyer_user_id) REFERENCES users(user_id),
    FOREIGN KEY (seller_user_id) REFERENCES users(user_id),
    FOREIGN KEY (symbol) REFERENCES instruments(symbol)
);

-- User portfolios/wallets for asset management
CREATE TABLE portfolios (
    id BIGSERIAL PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL,
    currency VARCHAR(10) NOT NULL,
    available_balance DECIMAL(20,8) DEFAULT 0 CHECK (available_balance >= 0),
    locked_balance DECIMAL(20,8) DEFAULT 0 CHECK (locked_balance >= 0),
    total_balance DECIMAL(20,8) GENERATED ALWAYS AS (available_balance + locked_balance) STORED,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    UNIQUE(user_id, currency),
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- Balance history for audit trail
CREATE TABLE balance_history (
    id BIGSERIAL PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL,
    currency VARCHAR(10) NOT NULL,
    transaction_type VARCHAR(30) NOT NULL CHECK (transaction_type IN ('DEPOSIT', 'WITHDRAWAL', 'TRADE_BUY', 'TRADE_SELL', 'FEE', 'INTEREST', 'ADJUSTMENT')),
    amount DECIMAL(20,8) NOT NULL,
    balance_before DECIMAL(20,8) NOT NULL,
    balance_after DECIMAL(20,8) NOT NULL,
    reference_id VARCHAR(50), -- Order ID or external transaction ID
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- Market data for price history and analytics
CREATE TABLE market_data (
    id BIGSERIAL PRIMARY KEY,
    symbol VARCHAR(20) NOT NULL,
    timestamp TIMESTAMP NOT NULL,
    open_price DECIMAL(20,8) NOT NULL,
    high_price DECIMAL(20,8) NOT NULL,
    low_price DECIMAL(20,8) NOT NULL,
    close_price DECIMAL(20,8) NOT NULL,
    volume DECIMAL(20,8) NOT NULL DEFAULT 0,
    trade_count INTEGER DEFAULT 0,
    interval_type VARCHAR(10) NOT NULL CHECK (interval_type IN ('1m', '5m', '15m', '1h', '4h', '1d')),
    
    UNIQUE(symbol, timestamp, interval_type),
    FOREIGN KEY (symbol) REFERENCES instruments(symbol)
);

-- Risk limits and controls
CREATE TABLE risk_limits (
    id BIGSERIAL PRIMARY KEY,
    user_id VARCHAR(50) NOT NULL,
    symbol VARCHAR(20),
    limit_type VARCHAR(30) NOT NULL CHECK (limit_type IN ('POSITION', 'ORDER_VALUE', 'DAILY_LOSS', 'OPEN_ORDERS')),
    limit_value DECIMAL(20,8) NOT NULL,
    current_value DECIMAL(20,8) DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (user_id) REFERENCES users(user_id),
    FOREIGN KEY (symbol) REFERENCES instruments(symbol)
);

-- Audit log for compliance and monitoring
CREATE TABLE audit_log (
    id BIGSERIAL PRIMARY KEY,
    user_id VARCHAR(50),
    action VARCHAR(50) NOT NULL,
    resource_type VARCHAR(50) NOT NULL,
    resource_id VARCHAR(50),
    old_values JSONB,
    new_values JSONB,
    ip_address INET,
    user_agent TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);

-- System configuration and parameters
CREATE TABLE system_config (
    id BIGSERIAL PRIMARY KEY,
    config_key VARCHAR(100) UNIQUE NOT NULL,
    config_value TEXT NOT NULL,
    description TEXT,
    is_encrypted BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ========================================
-- INDEXES FOR HIGH PERFORMANCE
-- ========================================

-- User indexes
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_status ON users(account_status);

-- Order indexes for ultra-fast lookups
CREATE INDEX idx_orders_user_id ON orders(user_id);
CREATE INDEX idx_orders_symbol ON orders(symbol);
CREATE INDEX idx_orders_status ON orders(status);
CREATE INDEX idx_orders_side_symbol ON orders(side, symbol);
CREATE INDEX idx_orders_created_at ON orders(created_at);
CREATE INDEX idx_orders_symbol_status_side ON orders(symbol, status, side);
CREATE INDEX idx_orders_active_orders ON orders(symbol, status, side, price) WHERE status IN ('OPEN', 'PARTIALLY_FILLED');

-- Trade indexes for reporting and analytics
CREATE INDEX idx_trades_symbol ON trades(symbol);
CREATE INDEX idx_trades_created_at ON trades(created_at);
CREATE INDEX idx_trades_buyer_user ON trades(buyer_user_id);
CREATE INDEX idx_trades_seller_user ON trades(seller_user_id);
CREATE INDEX idx_trades_symbol_time ON trades(symbol, created_at);

-- Portfolio indexes
CREATE INDEX idx_portfolios_user_currency ON portfolios(user_id, currency);
CREATE INDEX idx_balance_history_user ON balance_history(user_id, created_at);

-- Market data indexes
CREATE INDEX idx_market_data_symbol_time ON market_data(symbol, timestamp DESC);
CREATE INDEX idx_market_data_interval ON market_data(symbol, interval_type, timestamp DESC);

-- Risk management indexes
CREATE INDEX idx_risk_limits_user ON risk_limits(user_id, is_active);
CREATE INDEX idx_audit_log_user_time ON audit_log(user_id, created_at DESC);

-- ========================================
-- PARTITIONING FOR SCALABILITY
-- ========================================

-- Partition market_data by time for better performance
-- This will be implemented based on data volume requirements

-- ========================================
-- TRIGGERS FOR AUTOMATIC UPDATES
-- ========================================

-- Update timestamp trigger function
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Apply update triggers
CREATE TRIGGER update_users_updated_at BEFORE UPDATE ON users FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_instruments_updated_at BEFORE UPDATE ON instruments FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_orders_updated_at BEFORE UPDATE ON orders FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_portfolios_updated_at BEFORE UPDATE ON portfolios FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_risk_limits_updated_at BEFORE UPDATE ON risk_limits FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
CREATE TRIGGER update_system_config_updated_at BEFORE UPDATE ON system_config FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Insert sample instruments
INSERT INTO instruments (symbol, name, base_currency, quote_currency, tick_size, min_quantity) VALUES
('BTCUSD', 'Bitcoin/US Dollar', 'BTC', 'USD', 0.01, 0.0001),
('ETHUSD', 'Ethereum/US Dollar', 'ETH', 'USD', 0.01, 0.001),
('ADAUSD', 'Cardano/US Dollar', 'ADA', 'USD', 0.0001, 1.0),
('SOLUSD', 'Solana/US Dollar', 'SOL', 'USD', 0.01, 0.01),
('DOTUSD', 'Polkadot/US Dollar', 'DOT', 'USD', 0.01, 0.1);

-- Insert system configuration
INSERT INTO system_config (config_key, config_value, description) VALUES
('trading.enabled', 'true', 'Global trading enable/disable'),
('market.session', 'open', 'Current market session status'),
('risk.max_open_orders', '100', 'Maximum open orders per user'),
('fees.maker_rate', '0.001', 'Maker fee rate (0.1%)'),
('fees.taker_rate', '0.002', 'Taker fee rate (0.2%)');