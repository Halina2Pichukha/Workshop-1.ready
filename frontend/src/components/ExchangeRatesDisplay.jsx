import { useState, useEffect } from 'react';
import api from '../services/api';
import './ExchangeRatesDisplay.css';

function ExchangeRatesDisplay() {
  const [rates, setRates] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [lastUpdated, setLastUpdated] = useState(null);

  const fetchExchangeRates = async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await api.getExchangeRates();
      setRates(data.rates || []);
      setLastUpdated(new Date().toLocaleTimeString());
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchExchangeRates();
  }, []);

  const handleRefresh = () => {
    fetchExchangeRates();
  };

  if (loading && rates.length === 0) {
    return <div className="exchange-rates-display loading">Loading exchange rates...</div>;
  }

  if (error) {
    return (
      <div className="exchange-rates-display error">
        <p>Error: {error}</p>
        <button onClick={handleRefresh} className="refresh-button">Retry</button>
      </div>
    );
  }

  return (
    <div className="exchange-rates-display">
      <div className="header">
        <h2>📊 Exchange Rates</h2>
        <div className="header-actions">
          {lastUpdated && <span className="last-updated">Last updated: {lastUpdated}</span>}
          <button 
            onClick={handleRefresh} 
            className="refresh-button"
            disabled={loading}
          >
            {loading ? '🔄 Refreshing...' : '🔄 Refresh'}
          </button>
        </div>
      </div>

      {rates.length === 0 ? (
        <p className="empty-state">No exchange rates available. Add at least 2 currencies to see rates!</p>
      ) : (
        <div className="rates-container">
          <div className="rates-table">
            <div className="rates-header">
              <div className="rate-cell">From</div>
              <div className="rate-cell">To</div>
              <div className="rate-cell">Rate</div>
              <div className="rate-cell">Inverse</div>
            </div>
            {rates.map((rate, index) => (
              <div key={index} className="rate-row">
                <div className="rate-cell from-currency">
                  <span className="currency-badge">{rate.fromCurrency}</span>
                </div>
                <div className="rate-cell to-currency">
                  <span className="currency-badge">{rate.toCurrency}</span>
                </div>
                <div className="rate-cell rate-value">
                  {rate.rate.toFixed(4)}
                </div>
                <div className="rate-cell inverse-value">
                  {(1 / rate.rate).toFixed(4)}
                </div>
              </div>
            ))}
          </div>
          <div className="rates-summary">
            <p>Total exchange rate pairs: <strong>{rates.length}</strong></p>
          </div>
        </div>
      )}
    </div>
  );
}

export default ExchangeRatesDisplay;
