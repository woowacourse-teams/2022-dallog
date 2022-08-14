import { ThemeProvider } from '@emotion/react';
import { createRoot } from 'react-dom/client';
import { QueryClient, QueryClientProvider } from 'react-query';
import { RecoilRoot } from 'recoil';

import GlobalStyle from '@/styles/GlobalStyle';
import theme from '@/styles/theme';

import { worker } from '@/mocks/browser';

import App from './App';

if (process.env.NODE_ENV === 'development') {
  worker.start();
}

const queryClient = new QueryClient();

const root = document.getElementById('root') as HTMLElement;

const rootElement = createRoot(root);

rootElement.render(
  <RecoilRoot>
    <QueryClientProvider client={queryClient}>
      <ThemeProvider theme={theme}>
        <GlobalStyle />
        <App />
      </ThemeProvider>
    </QueryClientProvider>
  </RecoilRoot>
);
