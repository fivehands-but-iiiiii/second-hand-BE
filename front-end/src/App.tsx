import { useState } from 'react';
import reactLogo from './assets/react.svg';
import viteLogo from '/vite.svg';
import './App.css';
import GlobalStyle from './styles/globalStyles';

function App() {
  const [count, setCount] = useState(0);

  return (
    <>
      <GlobalStyle />
      <button>버튼</button>
    </>
  );
}

export default App;
