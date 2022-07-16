import { ThemeProvider } from '@emotion/react';
import ReactDOM from 'react-dom';
import { QueryClient, QueryClientProvider } from 'react-query';

import GlobalStyle from '@/styles/GlobalStyle';
import theme from '@/styles/theme';

import { worker } from '@/mocks/browser';

import App from './App';

if (process.env.NODE_ENV === 'development') {
  worker.start();
}

const queryClient = new QueryClient();
const rootElement = document.getElementById('root');

ReactDOM.render(
  <QueryClientProvider client={queryClient}>
    <ThemeProvider theme={theme}>
      <GlobalStyle />
      <App />
    </ThemeProvider>
  </QueryClientProvider>,
  rootElement
);
