import ReactDOM from 'react-dom';
import { ThemeProvider } from '@emotion/react';

import theme from '@/styles/theme';
import GlobalStyle from '@/styles/GlobalStyle';

import App from '@/App';

const rootElement = document.getElementById('root');

ReactDOM.render(
  <ThemeProvider theme={theme}>
    <GlobalStyle />
    <App />
  </ThemeProvider>,
  rootElement
);
