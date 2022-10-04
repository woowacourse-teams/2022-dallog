import { useTheme } from '@emotion/react';
import { lazy, Suspense, useRef, useState } from 'react';

import useCalendar from '@/hooks/useCalendar';
import useToggle from '@/hooks/useToggle';

import Button from '@/components/@common/Button/Button';
import Fieldset from '@/components/@common/Fieldset/Fieldset';
import ModalPortal from '@/components/@common/ModalPortal/ModalPortal';
import PageLayout from '@/components/@common/PageLayout/PageLayout';
import Spinner from '@/components/@common/Spinner/Spinner';
import CategoryAddModal from '@/components/CategoryAddModal/CategoryAddModal';
import CategoryListFallback from '@/components/CategoryList/CategoryList.fallback';

import { DAYS } from '@/constants/date';

import { extractDateTime, getToday } from '@/utils/date';

import { MdKeyboardArrowLeft, MdKeyboardArrowRight, MdSearch } from 'react-icons/md';

import {
  buttonStyle,
  calendarGridStyle,
  calendarHeaderStyle,
  calendarStyle,
  categoryHeaderStyle,
  categoryPageStyle,
  categoryStyle,
  controlStyle,
  dateStyle,
  dateTextStyle,
  dayBarGridStyle,
  dayBarStyle,
  monthPickerStyle,
  navButtonStyle,
  navButtonTitleStyle,
  searchButtonStyle,
  searchFieldsetStyle,
  searchFormStyle,
  searchInputStyle,
  spinnerStyle,
  todayButtonStyle,
  waitingNavStyle,
} from './CategoryPage.styles';

const CategoryList = lazy(() => import('@/components/CategoryList/CategoryList'));

function CategoryPage() {
  const theme = useTheme();

  const keywordRef = useRef<HTMLInputElement>(null);

  const [keyword, setKeyword] = useState('');

  const { state: isCategoryAddModalOpen, toggleState: toggleCategoryAddModalOpen } = useToggle();

  const { calendar, currentDateTime, moveToBeforeMonth, moveToToday, moveToNextMonth } =
    useCalendar();

  const { year: currentYear, month: currentMonth } = extractDateTime(currentDateTime);
  const rowNum = Math.ceil(calendar.length / 7);

  const handleSubmitCategorySearchForm = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    if (!(keywordRef.current instanceof HTMLInputElement)) {
      return;
    }

    setKeyword((keywordRef.current as HTMLInputElement).value);
  };

  const handleClickCategoryAddButton = () => {
    toggleCategoryAddModalOpen();
  };

  return (
    <PageLayout>
      <div css={categoryPageStyle}>
        <div css={categoryStyle}>
          <ModalPortal isOpen={isCategoryAddModalOpen} closeModal={toggleCategoryAddModalOpen}>
            <CategoryAddModal closeModal={toggleCategoryAddModalOpen} />
          </ModalPortal>
          <h1 css={categoryHeaderStyle}>카테고리</h1>
          <div css={controlStyle}>
            <form css={searchFormStyle} onSubmit={handleSubmitCategorySearchForm}>
              <Button type="submit" cssProp={searchButtonStyle}>
                <MdSearch size={20} />
              </Button>
              <Fieldset
                placeholder="제목 검색"
                cssProp={{ div: searchFieldsetStyle, input: searchInputStyle }}
                refProp={keywordRef}
              />
            </form>
            <Button cssProp={buttonStyle(theme)} onClick={handleClickCategoryAddButton}>
              추가
            </Button>
          </div>
          <Suspense fallback={<CategoryListFallback />}>
            <CategoryList keyword={keyword} />
          </Suspense>
        </div>
        <div css={calendarStyle}>
          <div css={calendarHeaderStyle}>
            {currentYear}년 {currentMonth}월
            <div css={waitingNavStyle}>
              <div css={spinnerStyle}>
                <Spinner size={4} />
                일정을 가져오고 있습니다.
              </div>
              <div css={monthPickerStyle}>
                <Button cssProp={navButtonStyle} onClick={moveToBeforeMonth}>
                  <MdKeyboardArrowLeft />
                  <span css={navButtonTitleStyle}>전 달</span>
                </Button>
                <Button cssProp={todayButtonStyle} onClick={moveToToday}>
                  오늘
                </Button>
                <Button cssProp={navButtonStyle} onClick={moveToNextMonth}>
                  <MdKeyboardArrowRight />
                  <span css={navButtonTitleStyle}>다음 달</span>
                </Button>
              </div>
            </div>
          </div>
          <div css={dayBarGridStyle}>
            {DAYS.map((day) => (
              <span key={`day#${day}`} css={dayBarStyle(theme, day)}>
                {day}
              </span>
            ))}
          </div>
          <div css={calendarGridStyle(rowNum)}>
            {calendar.map((dateTime) => {
              const { month, date, day } = extractDateTime(dateTime);

              return (
                <div key={dateTime}>
                  <div css={dateStyle(theme, day)}>
                    <span
                      css={dateTextStyle(
                        theme,
                        day,
                        currentMonth === month,
                        dateTime === getToday()
                      )}
                    >
                      {date}
                    </span>
                  </div>
                </div>
              );
            })}
          </div>
        </div>
      </div>
    </PageLayout>
  );
}

export default CategoryPage;
