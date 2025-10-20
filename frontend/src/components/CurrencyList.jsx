import { useState, useEffect } from 'react';
import api from '../services/api';
import './CurrencyList.css';

function CurrencyList() {
  const [currencies, setCurrencies] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  const fetchCurrencies = async () => {
    setLoading(true);
    setError(null);
    try {
      const data = await api.getCurrencies();
      setCurrencies(data.currencies || []);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchCurrencies();
  }, []);

  if (loading) {
    return <div className="currency-list loading">Loading currencies...</div>;
  }

  if (error) {
    return (
      <div className="currency-list error">
        <p>Error: {error}</p>
        <button onClick={fetchCurrencies}>Retry</button>
      </div>
    );
  }

  return (
    <div className="currency-list">
      <h2>Tracked Currencies</h2>
      {currencies.length === 0 ? (
        <p className="empty-state">No currencies tracked yet. Add one using the form below!</p>
      ) : (
        <div className="currency-grid">
          {currencies.map((currency) => (
            <div key={currency} className="currency-item">
              {currency}
            </div>
          ))}
        </div>
      )}
      <button className="refresh-button" onClick={fetchCurrencies}>
        🔄 Refresh
      </button>
    </div>
  );
}

export default CurrencyList;
