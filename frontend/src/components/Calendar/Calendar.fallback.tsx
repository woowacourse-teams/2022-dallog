import { CalendarControllerType } from '@/hooks/useCalendar';
import useRootFontSize from '@/hooks/useRootFontSize';

import { CategoryType } from '@/@types/category';

import theme from '@/styles/theme';

import Button from '@/components/@common/Button/Button';
import Responsive from '@/components/@common/Responsive/Responsive';
import Spinner from '@/components/@common/Spinner/Spinner';
import DateCell from '@/components/DateCell/DateCell';

import { DAYS } from '@/constants/date';
import { RESPONSIVE } from '@/constants/style';

import { MdKeyboardArrowLeft, MdKeyboardArrowRight } from 'react-icons/md';

import {
  calendarGridStyle,
  calendarHeaderStyle,
  dayGridStyle,
  dayStyle,
  hintStyle,
  monthPickerStyle,
  navButtonStyle,
  navButtonTitleStyle,
  navStyle,
  spinnerStyle,
  todayButtonStyle,
} from './Calendar.styles';

interface CalendarFallbackProps {
  calendarController: CalendarControllerType;
  setDateInfo?: React.Dispatch<React.SetStateAction<string>>;
  handleClickDateCell?: () => void;
  category?: Pick<CategoryType, 'id' | 'name'>;
  readonly?: boolean;
}

function CalendarFallback({
  calendarController,
  setDateInfo,
  handleClickDateCell,
  category,
  readonly = false,
}: CalendarFallbackProps) {
  const rootFontSize = useRootFontSize();

  const {
    calendar,
    currentMonth,
    currentYear,
    dateCellRef,
    moveToBeforeMonth,
    moveToNextMonth,
    moveToToday,
    rowCount,
  } = calendarController;

  return (
    <>
      {category && !category.id && (
        <div css={hintStyle}>클릭한 카테고리의 일정을 확인할 수 있어요</div>
      )}
      <div css={calendarHeaderStyle}>
        {`${currentYear}년 ${currentMonth}월${
          category && category.id ? ` \u00A0☾\u00A0 ${category?.name}` : ''
        }`}
        <div css={navStyle}>
          {category?.id !== 0 && (
            <div css={spinnerStyle}>
              <Spinner size={rootFontSize} />
              <Responsive type={RESPONSIVE.LAPTOP.DEVICE}>
                <span>일정을 가져오고 있습니다.</span>
              </Responsive>
            </div>
          )}
          <div css={monthPickerStyle}>
            <Button cssProp={navButtonStyle} onClick={moveToBeforeMonth} aria-label="이전 달">
              <MdKeyboardArrowLeft />
              <span css={navButtonTitleStyle}>전 달</span>
            </Button>
            <Button cssProp={todayButtonStyle} onClick={moveToToday} aria-label="이번 달">
              오늘
            </Button>
            <Button cssProp={navButtonStyle} onClick={moveToNextMonth} aria-label="다음 달">
              <MdKeyboardArrowRight />
              <span css={navButtonTitleStyle}>다음 달</span>
            </Button>
          </div>
        </div>
      </div>
      <div css={dayGridStyle}>
        {DAYS.map((day) => (
          <span key={`day#${day}`} css={dayStyle(theme, day)}>
            {day}
          </span>
        ))}
      </div>
      <div css={calendarGridStyle(rowCount)}>
        {calendar.map((dateTime) => {
          return (
            <DateCell
              key={dateTime}
              dateTime={dateTime}
              currentMonth={currentMonth}
              dateCellRef={dateCellRef}
              setDateInfo={setDateInfo}
              onClick={handleClickDateCell}
              readonly={readonly}
            />
          );
        })}
      </div>
    </>
  );
}

export default CalendarFallback;
