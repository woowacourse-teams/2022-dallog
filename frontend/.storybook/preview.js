import { ThemeProvider } from '@emotion/react';
import { QueryClient, QueryClientProvider } from 'react-query';

import GlobalStyle from '@/styles/GlobalStyle';
import theme from '@/styles/theme';

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
    <ThemeProvider theme={theme}>
      <GlobalStyle />
      <QueryClientProvider client={queryClient}>
        <Story />
      </QueryClientProvider>
    </ThemeProvider>
  ),
];
