import { ThemeProvider } from '@emotion/react';
import { createRoot } from 'react-dom/client';
import { RecoilRoot } from 'recoil';

import GlobalStyle from '@/styles/GlobalStyle';
import theme from '@/styles/theme';

import { worker } from '@/mocks/browser';

import App from './App';

if (process.env.NODE_ENV === 'development') {
  worker.start();
}

const root = document.getElementById('root') as HTMLElement;

const rootElement = createRoot(root);

rootElement.render(
  <RecoilRoot>
    <ThemeProvider theme={theme}>
      <GlobalStyle />
      <App />
    </ThemeProvider>
  </RecoilRoot>
);
