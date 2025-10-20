import { useState, useEffect } from 'react';
import CurrencyList from './components/CurrencyList';
import AddCurrency from './components/AddCurrency';
import CurrencyConverter from './components/CurrencyConverter';
import ExchangeRatesDisplay from './components/ExchangeRatesDisplay';
import api from './services/api';
import './App.css';

function App() {
  const [currencies, setCurrencies] = useState([]);
  const [refreshKey, setRefreshKey] = useState(0);

  useEffect(() => {
    const fetchCurrencies = async () => {
      try {
        const data = await api.getCurrencies();
        setCurrencies(data.currencies || []);
      } catch (error) {
        console.error('Error fetching currencies:', error);
      }
    };

    fetchCurrencies();
  }, [refreshKey]);

  const handleCurrencyAdded = () => {
    setRefreshKey(prev => prev + 1);
  };

  return (
    <div className="app">
      <header className="app-header">
        <h1>💱 Currency Exchange Rates Provider</h1>
        <p className="subtitle">Real-time currency conversion service</p>
      </header>

      <main className="app-main">
        <div className="container">
          <CurrencyList key={refreshKey} />
          <AddCurrency onCurrencyAdded={handleCurrencyAdded} />
          <CurrencyConverter currencies={currencies} />
          <ExchangeRatesDisplay key={refreshKey} />
        </div>
      </main>

      <footer className="app-footer">
        <p>Workshop Project - Spring Boot + React | In-Memory Storage</p>
      </footer>
    </div>
  );
}

export default App;
