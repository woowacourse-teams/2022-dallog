import { ThemeProvider } from '@emotion/react';
import { createRoot } from 'react-dom/client';
import { RecoilRoot } from 'recoil';

import GlobalStyle from '@/styles/GlobalStyle';
import theme from '@/styles/theme';

import App from './App';

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
