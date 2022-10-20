import { ThemeProvider } from '@emotion/react';
import { createRoot } from 'react-dom/client';
import { QueryClient, QueryClientProvider } from 'react-query';
import { ReactQueryDevtools } from 'react-query/devtools';
import { BrowserRouter as Router } from 'react-router-dom';
import { RecoilRoot } from 'recoil';

import GlobalStyle from '@/styles/GlobalStyle';
import theme from '@/styles/theme';

import ErrorBoundary from '@/components/@common/ErrorBoundary/ErrorBoundary';

import App from './App';

const root = document.getElementById('root') as HTMLElement;

const rootElement = createRoot(root);

const queryClient = new QueryClient();

rootElement.render(
  <RecoilRoot>
    <ThemeProvider theme={theme}>
      <GlobalStyle />
      <Router>
        <ErrorBoundary>
          <QueryClientProvider client={queryClient}>
            <App />
            <ReactQueryDevtools initialIsOpen={false} />
          </QueryClientProvider>
        </ErrorBoundary>
      </Router>
    </ThemeProvider>
  </RecoilRoot>
);
