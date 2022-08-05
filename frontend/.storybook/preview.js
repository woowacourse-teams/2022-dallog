import { ThemeProvider } from '@emotion/react';
import { QueryClient, QueryClientProvider } from 'react-query';
import { BrowserRouter as Router } from 'react-router-dom';
import { RecoilRoot } from 'recoil';

import GlobalStyle from '../src/styles/GlobalStyle';
import theme from '../src/styles/theme';

const queryClient = new QueryClient();

export const parameters = {
  actions: { argTypesRegex: '^on[A-Z].*' },
  controls: {
    matchers: {
      color: /(background|color)$/i,
      date: /Date$/,
    },
  },
};

export const decorators = [
  (Story) => (
    <RecoilRoot>
      <ThemeProvider theme={theme}>
        <GlobalStyle />

        <QueryClientProvider client={queryClient}>
          <Router>
            <Story />
          </Router>
        </QueryClientProvider>
      </ThemeProvider>
    </RecoilRoot>
  ),
];
