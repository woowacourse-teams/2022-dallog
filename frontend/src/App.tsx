import { Route, BrowserRouter as Router, Routes } from 'react-router-dom';

import CalendarPage from '@/pages/CalendarPage/CalendarPage';
import CategoryPage from '@/pages/CategoryPage/CategoryPage';
import MyPage from '@/pages/MyPage/MyPage';
import StartPage from '@/pages/StartPage/StartPage';

import { PATH } from '@/constants';

function App() {
  return (
    <Router>
      <Routes>
        <Route path={PATH.CALENDAR_PAGE} element={<CalendarPage />} />
        <Route path={PATH.START_PAGE} element={<StartPage />} />
        <Route path={PATH.CATEGORY} element={<CategoryPage />} />
        <Route path={PATH.PROFILE} element={<MyPage />} />
      </Routes>
    </Router>
  );
}

export default App;
