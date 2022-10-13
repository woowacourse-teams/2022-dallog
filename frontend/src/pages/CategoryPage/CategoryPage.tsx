import { useTheme } from '@emotion/react';
import { lazy, Suspense, useRef, useState } from 'react';

import { useGetSchedulesWithCategory } from '@/hooks/@queries/category';
import useCalendar from '@/hooks/useCalendar';
import useModalPosition from '@/hooks/useModalPosition';
import useSchedulePriority from '@/hooks/useSchedulePriority';
import useToggle from '@/hooks/useToggle';

import { CategoryType } from '@/@types/category';

import Button from '@/components/@common/Button/Button';
import Fieldset from '@/components/@common/Fieldset/Fieldset';
import ModalPortal from '@/components/@common/ModalPortal/ModalPortal';
import PageLayout from '@/components/@common/PageLayout/PageLayout';
import Spinner from '@/components/@common/Spinner/Spinner';
import CategoryAddModal from '@/components/CategoryAddModal/CategoryAddModal';
import CategoryListFallback from '@/components/CategoryList/CategoryList.fallback';
import MoreScheduleModal from '@/components/MoreScheduleModal/MoreScheduleModal';

import { CALENDAR } from '@/constants';
import { DAYS } from '@/constants/date';
import { SCHEDULE, TRANSPARENT } from '@/constants/style';

import {
  checkAllDay,
  extractDateTime,
  getDayOffsetDateTime,
  getISODateString,
  getToday,
} from '@/utils/date';

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
  itemWithBackgroundStyle,
  itemWithoutBackgroundStyle,
  monthPickerStyle,
  moreStyle,
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
  const dateRef = useRef<HTMLDivElement>(null);

  const [keyword, setKeyword] = useState('');
  const [category, setCategory] = useState<Pick<CategoryType, 'id' | 'name'>>({ id: 0, name: '' });
  const [moreScheduleDateTime, setMoreScheduleDateTime] = useState('');

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

  const { calendarWithPriority, getLongTermSchedulesWithPriority, getSingleSchedulesWithPriority } =
    useSchedulePriority(calendar);

  const moreScheduleModal = useModalPosition();

  const { isLoading, data } = useGetSchedulesWithCategory({
    categoryId: category.id,
    startDateTime,
    endDateTime,
  });

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
              {currentYear}년 {currentMonth}월
              {!category.id &&
                '\u00A0\u00A0☾\u00A0\u00A0카테고리를 클릭하면 일정을 확인할 수 있습니다.'}
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
                const { month, date, day } = extractDateTime(dateTime);

                return (
                  <div key={dateTime}>
                    <div css={dateStyle(theme, day)} ref={dateRef}>
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

  const longTermSchedulesWithPriority = getLongTermSchedulesWithPriority(data.data.longTerms);
  const allDaySchedulesWithPriority = getSingleSchedulesWithPriority(data.data.allDays);
  const fewHourSchedulesWithPriority = getSingleSchedulesWithPriority(data.data.fewHours);

  const MAX_SCHEDULE_COUNT =
    dateRef.current !== null
      ? Math.floor(
          (dateRef.current.clientHeight - SCHEDULE.HEIGHT * 4) / (SCHEDULE.HEIGHT_WITH_MARGIN * 4)
        )
      : CALENDAR.MAX_SCHEDULE_COUNT;

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
            {currentYear}년 {currentMonth}월 &nbsp;☾&nbsp; {category.name}
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
              const { month, date, day } = extractDateTime(dateTime);
              const currentDate = getISODateString(dateTime);

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

                    {longTermSchedulesWithPriority.map(({ schedule, priority }) => {
                      const startDate = getISODateString(schedule.startDateTime);
                      const endDate = getISODateString(
                        checkAllDay(schedule.startDateTime, schedule.endDateTime)
                          ? getDayOffsetDateTime(schedule.endDateTime, -1)
                          : schedule.endDateTime
                      );
                      const { day: currentDay } = extractDateTime(dateTime);

                      if (
                        !(startDate <= currentDate && currentDate <= endDate) ||
                        priority === null
                      )
                        return;

                      return (
                        <div
                          key={`${currentDate}#${schedule.id}#longTerms`}
                          css={itemWithBackgroundStyle(
                            priority,
                            schedule.colorCode,
                            MAX_SCHEDULE_COUNT,
                            currentDate === endDate
                          )}
                        >
                          {(startDate === currentDate || currentDay === 0) &&
                            (schedule.title || CALENDAR.EMPTY_TITLE)}
                        </div>
                      );
                    })}

                    {allDaySchedulesWithPriority.map(({ schedule, priority }) => {
                      const startDate = getISODateString(schedule.startDateTime);

                      if (startDate !== currentDate || priority === null) return;

                      return (
                        <div
                          key={`${currentDate}#${schedule.id}#allDays`}
                          css={itemWithBackgroundStyle(
                            priority,
                            schedule.colorCode,
                            MAX_SCHEDULE_COUNT,
                            true
                          )}
                        >
                          {schedule.title || CALENDAR.EMPTY_TITLE}
                        </div>
                      );
                    })}

                    {fewHourSchedulesWithPriority.map(({ schedule, priority }) => {
                      const startDate = getISODateString(schedule.startDateTime);

                      if (startDate !== currentDate || priority === null) return;

                      return (
                        <div
                          key={`${currentDate}#${schedule.id}#fewHours`}
                          css={itemWithoutBackgroundStyle(
                            theme,
                            priority,
                            schedule.colorCode,
                            MAX_SCHEDULE_COUNT,
                            false
                          )}
                        >
                          {schedule.title || CALENDAR.EMPTY_TITLE}
                        </div>
                      );
                    })}

                    {calendarWithPriority[getISODateString(dateTime)].findIndex((el) => !el) + 1 >
                      MAX_SCHEDULE_COUNT && (
                      <span
                        css={moreStyle}
                        onClick={(e) =>
                          moreScheduleModal.handleClickOpen(e, () =>
                            setMoreScheduleDateTime(dateTime)
                          )
                        }
                      >
                        일정 더보기
                      </span>
                    )}
                  </div>
                  {dateTime === moreScheduleDateTime && (
                    <ModalPortal
                      isOpen={moreScheduleModal.isModalOpen}
                      closeModal={moreScheduleModal.toggleModalOpen}
                      dimmerBackground={TRANSPARENT}
                    >
                      <MoreScheduleModal
                        moreScheduleModalPos={moreScheduleModal.modalPos}
                        moreScheduleDateTime={moreScheduleDateTime}
                        longTermSchedulesWithPriority={longTermSchedulesWithPriority}
                        allDaySchedulesWithPriority={allDaySchedulesWithPriority}
                        fewHourSchedulesWithPriority={fewHourSchedulesWithPriority}
                      />
                    </ModalPortal>
                  )}
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
