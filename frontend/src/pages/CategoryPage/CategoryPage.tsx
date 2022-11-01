import { useState } from 'react';

import { useGetSchedulesWithCategory } from '@/hooks/@queries/category';
import useCalendar from '@/hooks/useCalendar';

import { CategoryType } from '@/@types/category';

import PageLayout from '@/components/@common/PageLayout/PageLayout';
import Calendar from '@/components/Calendar/Calendar';
import CalendarFallback from '@/components/Calendar/Calendar.fallback';
import CategoryControl from '@/components/CategoryControl/CategoryControl';

import { PAGE_LAYOUT } from '@/constants/style';

import { calendarStyle, categoryPageStyle } from './CategoryPage.styles';

function CategoryPage() {
  const [category, setCategory] = useState<Pick<CategoryType, 'id' | 'name'>>({ id: 0, name: '' });

  const calendarController = useCalendar();
  const { startDateTime, endDateTime } = calendarController;

  const { isLoading, data } = useGetSchedulesWithCategory({
    categoryId: category.id,
    startDateTime,
    endDateTime,
  });

  return (
    <PageLayout type={PAGE_LAYOUT.SIDEBAR}>
      <div css={categoryPageStyle}>
        <CategoryControl setCategory={setCategory} />
        <div css={calendarStyle}>
          {(isLoading || data === undefined) && (
            <CalendarFallback
              calendarController={calendarController}
              category={category}
              readonly
            />
          )}
          {data && (
            <Calendar
              calendarController={calendarController}
              scheduleResponse={data}
              category={category}
              readonly
            />
          )}
        </div>
      </div>
    </PageLayout>
  );
}

export default CategoryPage;
