import { Route, BrowserRouter as Router, Routes } from 'react-router-dom';

import CalendarPage from '@/pages/CalendarPage/CalendarPage';

import { PATH } from '@/constants';

function App() {
  return (
    <Router>
      <Routes>
        <Route path={PATH.CALENDAR_PAGE} element={<CalendarPage />} />
      </Routes>
    </Router>
  );
}

export default App;
