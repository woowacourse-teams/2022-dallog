import { useTheme } from '@emotion/react';
import { lazy, Suspense, useLayoutEffect, useRef, useState } from 'react';

import { useGetSchedulesWithCategory } from '@/hooks/@queries/category';
import useCalendar from '@/hooks/useCalendar';
import useRootFontSize from '@/hooks/useRootFontSize';
import useToggle from '@/hooks/useToggle';

import { CategoryType } from '@/@types/category';

import Button from '@/components/@common/Button/Button';
import Fieldset from '@/components/@common/Fieldset/Fieldset';
import ModalPortal from '@/components/@common/ModalPortal/ModalPortal';
import PageLayout from '@/components/@common/PageLayout/PageLayout';
import Spinner from '@/components/@common/Spinner/Spinner';
import CategoryAddModal from '@/components/CategoryAddModal/CategoryAddModal';
import CategoryListFallback from '@/components/CategoryList/CategoryList.fallback';
import DateCell from '@/components/DateCell/DateCell';

import { DAYS } from '@/constants/date';
import { PAGE_LAYOUT, SCHEDULE } from '@/constants/style';

import { extractDateTime } from '@/utils/date';

import getSchedulePriority from '@/domains/schedule';

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
  dayBarGridStyle,
  dayBarStyle,
  hintStyle,
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
  const dateCellRef = useRef<HTMLDivElement>(null);

  const [maxScheduleCount, setMaxScheduleCount] = useState(0);
  const [keyword, setKeyword] = useState('');
  const [category, setCategory] = useState<Pick<CategoryType, 'id' | 'name'>>({ id: 0, name: '' });
  const [hoveringScheduleId, setHoveringScheduleId] = useState('0');

  const rootFontSize = useRootFontSize();

  const { state: isCategoryAddModalOpen, toggleState: toggleCategoryAddModalOpen } = useToggle();

  const {
    calendar,
    currentDateTime,
    moveToBeforeMonth,
    moveToToday,
    moveToNextMonth,
    startDateTime,
    endDateTime,
  } = useCalendar();

  const { isLoading, data } = useGetSchedulesWithCategory({
    categoryId: category.id,
    startDateTime,
    endDateTime,
  });

  useLayoutEffect(() => {
    if (!(dateCellRef.current instanceof HTMLDivElement)) return;

    setMaxScheduleCount(
      Math.floor(
        (Math.floor(dateCellRef.current.clientHeight / 10) * 10 - SCHEDULE.HEIGHT * rootFontSize) /
          (SCHEDULE.HEIGHT_WITH_MARGIN * rootFontSize)
      )
    );
  }, [startDateTime, rootFontSize]);

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

  if (!category.id || isLoading || data === undefined) {
    return (
      <PageLayout type={PAGE_LAYOUT.SIDEBAR}>
        <div css={categoryPageStyle}>
          <div css={categoryStyle}>
            <ModalPortal isOpen={isCategoryAddModalOpen} closeModal={toggleCategoryAddModalOpen}>
              <CategoryAddModal closeModal={toggleCategoryAddModalOpen} />
            </ModalPortal>
            <h1 css={categoryHeaderStyle}>카테고리</h1>
            <div css={controlStyle}>
              <form css={searchFormStyle} onSubmit={handleSubmitCategorySearchForm}>
                <Button type="submit" cssProp={searchButtonStyle}>
                  <MdSearch size={rootFontSize * 5} />
                </Button>
                <Fieldset
                  placeholder="제목 찾기"
                  cssProp={{ div: searchFieldsetStyle, input: searchInputStyle }}
                  refProp={keywordRef}
                />
              </form>
              <Button cssProp={buttonStyle(theme)} onClick={handleClickCategoryAddButton}>
                추가
              </Button>
            </div>
            <Suspense fallback={<CategoryListFallback />}>
              <CategoryList keyword={keyword} setCategory={setCategory} />
            </Suspense>
          </div>
          <div css={calendarStyle}>
            {!category.id && <div css={hintStyle}>클릭한 카테고리의 일정을 확인할 수 있어요</div>}
            <div css={calendarHeaderStyle}>
              {`${currentYear}년 ${currentMonth}월`}
              <div css={waitingNavStyle}>
                {isLoading && (
                  <div css={spinnerStyle}>
                    <Spinner size={4} />
                    일정을 가져오고 있습니다.
                  </div>
                )}
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
                return (
                  <DateCell
                    key={dateTime}
                    dateTime={dateTime}
                    currentMonth={currentMonth}
                    dateCellRef={dateCellRef}
                    readonly
                  />
                );
              })}
            </div>
          </div>
        </div>
      </PageLayout>
    );
  }

  const { calendarWithPriority, getLongTermSchedulesWithPriority, getSingleSchedulesWithPriority } =
    getSchedulePriority(calendar);

  const schedulesWithPriority = {
    longTermSchedulesWithPriority: getLongTermSchedulesWithPriority(data.data.longTerms),
    allDaySchedulesWithPriority: getSingleSchedulesWithPriority(data.data.allDays),
    fewHourSchedulesWithPriority: getSingleSchedulesWithPriority(data.data.fewHours),
  };

  return (
    <PageLayout type={PAGE_LAYOUT.SIDEBAR}>
      <div css={categoryPageStyle}>
        <div css={categoryStyle}>
          <ModalPortal isOpen={isCategoryAddModalOpen} closeModal={toggleCategoryAddModalOpen}>
            <CategoryAddModal closeModal={toggleCategoryAddModalOpen} />
          </ModalPortal>
          <h1 css={categoryHeaderStyle}>카테고리</h1>
          <div css={controlStyle}>
            <form css={searchFormStyle} onSubmit={handleSubmitCategorySearchForm}>
              <Button type="submit" cssProp={searchButtonStyle}>
                <MdSearch size={rootFontSize * 5} />
              </Button>
              <Fieldset
                placeholder="제목 찾기"
                cssProp={{ div: searchFieldsetStyle, input: searchInputStyle }}
                refProp={keywordRef}
              />
            </form>
            <Button cssProp={buttonStyle(theme)} onClick={handleClickCategoryAddButton}>
              추가
            </Button>
          </div>
          <Suspense fallback={<CategoryListFallback />}>
            <CategoryList keyword={keyword} setCategory={setCategory} />
          </Suspense>
        </div>
        <div css={calendarStyle}>
          <div css={calendarHeaderStyle}>
            {`${currentYear}년 ${currentMonth}월 \u00A0☾\u00A0 ${category.name}`}
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
          <div css={dayBarGridStyle}>
            {DAYS.map((day) => (
              <span key={`day#${day}`} css={dayBarStyle(theme, day)}>
                {day}
              </span>
            ))}
          </div>
          <div css={calendarGridStyle(rowNum)}>
            {calendar.map((dateTime) => {
              return (
                <DateCell
                  key={dateTime}
                  dateTime={dateTime}
                  currentMonth={currentMonth}
                  dateCellRef={dateCellRef}
                  hoveringScheduleId={hoveringScheduleId}
                  setHoveringScheduleId={setHoveringScheduleId}
                  maxScheduleCount={maxScheduleCount}
                  calendarWithPriority={calendarWithPriority}
                  schedulesWithPriority={schedulesWithPriority}
                  readonly
                />
              );
            })}
          </div>
        </div>
      </div>
    </PageLayout>
  );
}

export default CategoryPage;
