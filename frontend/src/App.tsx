import { Route, BrowserRouter as Router, Routes } from 'react-router-dom';

import CalendarPage from '@/pages/CalendarPage/CalendarPage';
import StartPage from '@/pages/StartPage/StartPage';

import { PATH } from '@/constants';

function App() {
  return (
    <Router>
      <Routes>
        <Route path={PATH.CALENDAR_PAGE} element={<CalendarPage />} />
        <Route path={PATH.START_PAGE} element={<StartPage />} />
      </Routes>
    </Router>
  );
}

export default App;
