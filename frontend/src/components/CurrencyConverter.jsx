import { useState, useEffect } from 'react';
import api from '../services/api';
import './CurrencyConverter.css';

function CurrencyConverter({ currencies }) {
  const [amount, setAmount] = useState('100');
  const [fromCurrency, setFromCurrency] = useState('');
  const [toCurrency, setToCurrency] = useState('');
  const [result, setResult] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  // Set default currencies when available
  useEffect(() => {
    if (currencies.length > 0) {
      if (!fromCurrency) setFromCurrency(currencies[0]);
      if (!toCurrency && currencies.length > 1) {
        setToCurrency(currencies[1]);
      } else if (!toCurrency) {
        setToCurrency(currencies[0]);
      }
    }
  }, [currencies, fromCurrency, toCurrency]);

  const handleConvert = async (e) => {
    e.preventDefault();

    if (!amount || parseFloat(amount) < 0) {
      setError('Please enter a valid positive amount');
      return;
    }

    if (!fromCurrency || !toCurrency) {
      setError('Please select both currencies');
      return;
    }

    setLoading(true);
    setError('');
    setResult(null);

    try {
      const data = await api.convertCurrency(amount, fromCurrency, toCurrency);
      setResult(data);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  const swapCurrencies = () => {
    const temp = fromCurrency;
    setFromCurrency(toCurrency);
    setToCurrency(temp);
    setResult(null);
  };

  return (
    <div className="currency-converter">
      <h2>Currency Converter</h2>

      {currencies.length < 2 ? (
        <p className="warning">⚠️ Add at least 2 currencies to start converting!</p>
      ) : (
        <form onSubmit={handleConvert}>
          <div className="form-row">
            <div className="form-group">
              <label htmlFor="amount">Amount:</label>
              <input
                type="number"
                id="amount"
                value={amount}
                onChange={(e) => setAmount(e.target.value)}
                min="0"
                step="0.01"
                disabled={loading}
              />
            </div>
          </div>

          <div className="converter-row">
            <div className="form-group">
              <label htmlFor="fromCurrency">From:</label>
              <select
                id="fromCurrency"
                value={fromCurrency}
                onChange={(e) => setFromCurrency(e.target.value)}
                disabled={loading}
              >
                {currencies.map((currency) => (
                  <option key={currency} value={currency}>
                    {currency}
                  </option>
                ))}
              </select>
            </div>

            <button
              type="button"
              className="swap-button"
              onClick={swapCurrencies}
              disabled={loading}
              title="Swap currencies"
            >
              ⇄
            </button>

            <div className="form-group">
              <label htmlFor="toCurrency">To:</label>
              <select
                id="toCurrency"
                value={toCurrency}
                onChange={(e) => setToCurrency(e.target.value)}
                disabled={loading}
              >
                {currencies.map((currency) => (
                  <option key={currency} value={currency}>
                    {currency}
                  </option>
                ))}
              </select>
            </div>
          </div>

          <button type="submit" className="convert-button" disabled={loading}>
            {loading ? 'Converting...' : '💱 Convert'}
          </button>
        </form>
      )}

      {error && <div className="error-message">{error}</div>}

      {result && (
        <div className="result">
          <h3>Conversion Result</h3>
          <div className="result-details">
            <div className="result-row">
              <span className="label">Amount:</span>
              <span className="value">{result.amount} {result.fromCurrency}</span>
            </div>
            <div className="result-row">
              <span className="label">Exchange Rate:</span>
              <span className="value">{result.rate.toFixed(6)}</span>
            </div>
            <div className="result-row highlight">
              <span className="label">Converted Amount:</span>
              <span className="value">
                {result.convertedAmount.toFixed(2)} {result.toCurrency}
              </span>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}

export default CurrencyConverter;
