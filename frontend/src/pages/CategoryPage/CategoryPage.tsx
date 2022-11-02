import { useState } from 'react';

import { useGetSchedulesWithCategory } from '@/hooks/@queries/category';
import useCalendar from '@/hooks/useCalendar';

import { CategoryType } from '@/@types/category';

import PageLayout from '@/components/@common/PageLayout/PageLayout';
import Calendar from '@/components/Calendar/Calendar';
import CalendarFallback from '@/components/Calendar/Calendar.fallback';
import CategoryControl from '@/components/CategoryControl/CategoryControl';

import { PAGE_LAYOUT } from '@/constants/style';

import { calendarStyle, categoryPageStyle, hintStyle } from './CategoryPage.styles';

function CategoryPage() {
  const [category, setCategory] = useState<Pick<CategoryType, 'id' | 'name'>>({ id: 0, name: '' });

  const calendarController = useCalendar();
  const { startDateTime, endDateTime } = calendarController;

  const { isLoading, data } = useGetSchedulesWithCategory({
    categoryId: category.id,
    startDateTime,
    endDateTime,
  });

  if (category.id === 0) {
    return (
      <PageLayout type={PAGE_LAYOUT.SIDEBAR}>
        <div css={categoryPageStyle}>
          <CategoryControl setCategory={setCategory} />
          <div css={calendarStyle}>
            <div css={hintStyle}>클릭한 카테고리의 일정을 확인할 수 있어요</div>
            <CalendarFallback calendarController={calendarController} isLoading={false} readonly />
          </div>
        </div>
      </PageLayout>
    );
  }

  return (
    <PageLayout type={PAGE_LAYOUT.SIDEBAR}>
      <div css={categoryPageStyle}>
        <CategoryControl setCategory={setCategory} />
        <div css={calendarStyle}>
          {isLoading && (
            <CalendarFallback
              calendarController={calendarController}
              categoryName={category.name}
              readonly
            />
          )}
          {data && (
            <Calendar
              calendarController={calendarController}
              scheduleResponse={data}
              categoryName={category.name}
              readonly
            />
          )}
        </div>
      </div>
    </PageLayout>
  );
}

export default CategoryPage;
