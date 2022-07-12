import { QueryClient, QueryClientProvider } from 'react-query';

import CalendarPage from '@/pages/CalendarPage/CalendarPage';

const queryClient = new QueryClient();

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <CalendarPage />
    </QueryClientProvider>
  );
}

export default App;
