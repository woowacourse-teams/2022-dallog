import { ThemeProvider } from '@emotion/react';
import ReactDOM from 'react-dom';

import GlobalStyle from '@/styles/GlobalStyle';
import theme from '@/styles/theme';

import { worker } from '@/mocks/browser';

import App from './App';

if (process.env.NODE_ENV === 'development') {
  worker.start();
}

const rootElement = document.getElementById('root');

ReactDOM.render(
  <ThemeProvider theme={theme}>
    <GlobalStyle />
    <App />
  </ThemeProvider>,
  rootElement
);
