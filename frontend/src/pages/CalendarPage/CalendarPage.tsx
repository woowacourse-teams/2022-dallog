import { useTheme } from '@emotion/react';
import { AxiosError, AxiosResponse } from 'axios';
import { useRef, useState } from 'react';
import { useQuery } from 'react-query';
import { useRecoilValue } from 'recoil';

import useCalendar from '@/hooks/useCalendar';
import useSchedulePriority from '@/hooks/useSchedulePriority';
import useToggle from '@/hooks/useToggle';

import { ModalPosType } from '@/@types';
import { CalendarType } from '@/@types/calendar';
import { ScheduleResponseType, ScheduleType } from '@/@types/schedule';

import { userState } from '@/recoil/atoms';

import Button from '@/components/@common/Button/Button';
import ModalPortal from '@/components/@common/ModalPortal/ModalPortal';
import PageLayout from '@/components/@common/PageLayout/PageLayout';
import Spinner from '@/components/@common/Spinner/Spinner';
import DateModal from '@/components/DateModal/DateModal';
import ScheduleAddButton from '@/components/ScheduleAddButton/ScheduleAddButton';
import ScheduleAddModal from '@/components/ScheduleAddModal/ScheduleAddModal';
import ScheduleModal from '@/components/ScheduleModal/ScheduleModal';
import ScheduleModifyModal from '@/components/ScheduleModifyModal/ScheduleModifyModal';

import { CACHE_KEY, CALENDAR } from '@/constants';
import { DAYS } from '@/constants/date';
import { TRANSPARENT } from '@/constants/style';

import {
  getDayFromFormattedDate,
  getFormattedDate,
  getISODateString,
  getThisDate,
  getThisMonth,
} from '@/utils/date';

import scheduleApi from '@/api/schedule';

import { AiOutlineLeft, AiOutlineRight } from 'react-icons/ai';

import {
  calendarGrid,
  calendarHeader,
  calendarPage,
  dateBorder,
  dateText,
  dayBar,
  itemWithBackgroundStyle,
  itemWithoutBackgroundStyle,
  monthPicker,
  moreStyle,
  navBarGrid,
  navButton,
  navButtonTitle,
  spinnerStyle,
  todayButton,
  waitingNavStyle,
} from './CalendarPage.styles';

function CalendarPage() {
  const { accessToken } = useRecoilValue(userState);

  const theme = useTheme();

  const dateRef = useRef<HTMLDivElement>(null);

  const [hoveringId, setHoveringId] = useState('0');
  const [dateInfo, setDateInfo] = useState<CalendarType | null>(null);
  const [modalPos, setModalPos] = useState<ModalPosType>({});
  const [scheduleInfo, setScheduleInfo] = useState<ScheduleType | null>(null);
  const [moreDateInfo, setMoreDateInfo] = useState<CalendarType | null>(null);

  const {
    calendarMonth,
    current,
    moveToBeforeMonth,
    moveToToday,
    moveToNextMonth,
    startDate,
    endDate,
  } = useCalendar();

  const { getLongTermsPriority, getAllDaysPriority, getFewHoursPriority } =
    useSchedulePriority(calendarMonth);

  const { state: isScheduleAddModalOpen, toggleState: toggleScheduleAddModalOpen } = useToggle();
  const { state: isScheduleModalOpen, toggleState: toggleScheduleModalOpen } = useToggle();
  const { state: isScheduleModifyModalOpen, toggleState: toggleScheduleModifyModalOpen } =
    useToggle();
  const { state: isDateModalOpen, toggleState: toggleDateModalOpen } = useToggle();

  const { isLoading, data } = useQuery<AxiosResponse<ScheduleResponseType>, AxiosError>(
    [CACHE_KEY.SCHEDULES, current],
    () => scheduleApi.get(accessToken, startDate, endDate)
  );

  const rowNum = Math.ceil(calendarMonth.length / 7);

  const handleClickDate = (e: React.MouseEvent, info: CalendarType) => {
    if (e.target !== e.currentTarget) {
      return;
    }

    setDateInfo(info);
    toggleScheduleAddModalOpen();
  };

  const handleClickSchedule = (e: React.MouseEvent, info: ScheduleType) => {
    if (e.target !== e.currentTarget) {
      return;
    }

    setModalPos(calculateModalPos(e.clientX, e.clientY));
    setScheduleInfo(info);
    toggleScheduleModalOpen();
  };

  const calculateModalPos = (clickX: number, clickY: number) => {
    const position = { top: clickY, right: 0, bottom: 0, left: clickX };

    if (clickX > innerWidth / 2) {
      position.right = innerWidth - clickX;
      position.left = 0;
    }

    if (clickY > innerHeight / 2) {
      position.bottom = innerHeight - clickY;
      position.top = 0;
    }

    return position;
  };

  if (isLoading || data === undefined) {
    return (
      <PageLayout>
        <div css={calendarPage}>
          <div css={calendarHeader}>
            <span>
              {current.year}년 {current.month}월
            </span>
            <div css={waitingNavStyle}>
              <div css={spinnerStyle}>
                <Spinner size={4} />
                <span>일정을 가져오고 있습니다.</span>
              </div>
              <div css={monthPicker}>
                <Button cssProp={navButton} onClick={moveToBeforeMonth}>
                  <AiOutlineLeft />
                  <span css={navButtonTitle}>전 달</span>
                </Button>
                <Button cssProp={todayButton} onClick={moveToToday}>
                  오늘
                </Button>
                <Button cssProp={navButton} onClick={moveToNextMonth}>
                  <AiOutlineRight />
                  <span css={navButtonTitle}>다음 달</span>
                </Button>
              </div>
            </div>
          </div>
          <div css={navBarGrid}>
            {DAYS.map((day) => (
              <span key={day} css={dayBar(theme, day)}>
                {day}
              </span>
            ))}
          </div>
          <div css={calendarGrid(rowNum)}>
            {calendarMonth.map((info) => {
              const key = getFormattedDate(info.year, info.month, info.date);

              return (
                <div
                  key={key}
                  css={dateBorder(theme, info.day)}
                  onClick={(e) => handleClickDate(e, info)}
                  ref={dateRef}
                >
                  <span
                    css={dateText(
                      theme,
                      info.day,
                      current.month === info.month,
                      getThisMonth() === info.month && getThisDate() === info.date
                    )}
                  >
                    {info.date}
                  </span>
                </div>
              );
            })}
          </div>
          <ModalPortal isOpen={isScheduleAddModalOpen} closeModal={toggleScheduleAddModalOpen}>
            <ScheduleAddModal dateInfo={dateInfo} closeModal={toggleScheduleAddModalOpen} />
          </ModalPortal>
          <ScheduleAddButton onClick={toggleScheduleAddModalOpen} />
        </div>
      </PageLayout>
    );
  }

  const longTermsWithPriority = getLongTermsPriority(data.data.longTerms);
  const allDaysWithPriority = getAllDaysPriority(data.data.allDays);
  const fewHoursWithPriority = getFewHoursPriority(data.data.fewHours);

  const handleClickMoreButton = (e: React.MouseEvent, info: CalendarType) => {
    if (e.target !== e.currentTarget) {
      return;
    }

    setModalPos(calculateModalPos(e.clientX, e.clientY));
    setMoreDateInfo(info);
    toggleDateModalOpen();
  };

  const onMouseEnter = (scheduleId: string) => {
    setHoveringId(scheduleId);
  };

  const onMouseLeave = () => {
    setHoveringId('0');
  };

  const maxView =
    dateRef.current !== null
      ? Math.floor((dateRef.current.clientHeight - 20) / 22)
      : CALENDAR.MAX_VIEW;

  return (
    <PageLayout>
      <div css={calendarPage}>
        <div css={calendarHeader}>
          <span>
            {current.year}년 {current.month}월
          </span>
          <div css={monthPicker}>
            <Button cssProp={navButton} onClick={moveToBeforeMonth}>
              <AiOutlineLeft />
              <span css={navButtonTitle}>전 달</span>
            </Button>
            <Button cssProp={todayButton} onClick={moveToToday}>
              오늘
            </Button>
            <Button cssProp={navButton} onClick={moveToNextMonth}>
              <AiOutlineRight />
              <span css={navButtonTitle}>다음 달</span>
            </Button>
          </div>
        </div>
        <div css={navBarGrid}>
          {DAYS.map((day) => (
            <span key={day} css={dayBar(theme, day)}>
              {day}
            </span>
          ))}
        </div>
        <div css={calendarGrid(rowNum)}>
          {calendarMonth.map((info) => {
            const key = getFormattedDate(info.year, info.month, info.date);

            return (
              <>
                <div
                  key={key}
                  css={dateBorder(theme, info.day)}
                  onClick={(e) => handleClickDate(e, info)}
                  ref={dateRef}
                >
                  <span
                    css={dateText(
                      theme,
                      info.day,
                      current.month === info.month,
                      getThisMonth() === info.month && getThisDate() === info.date
                    )}
                  >
                    {info.date}
                  </span>

                  {longTermsWithPriority.map((el) => {
                    const startDate = getISODateString(el.schedule.startDateTime);
                    const endDate = getISODateString(el.schedule.endDateTime);
                    const nowDate = getFormattedDate(info.year, info.month, info.date);
                    const nowDay = getDayFromFormattedDate(nowDate);

                    if (startDate <= nowDate && nowDate <= endDate && el.priority >= maxView) {
                      return (
                        <span css={moreStyle} onClick={(e) => handleClickMoreButton(e, info)}>
                          일정 더보기
                        </span>
                      );
                    }

                    return (
                      startDate <= nowDate &&
                      nowDate <= endDate && (
                        <div
                          key={`${nowDate}#${el.schedule.id}`}
                          css={itemWithBackgroundStyle(
                            el.priority,
                            el.schedule.colorCode,
                            hoveringId === el.schedule.id,
                            maxView
                          )}
                          onMouseEnter={() => onMouseEnter(el.schedule.id)}
                          onClick={(e) => handleClickSchedule(e, el.schedule)}
                          onMouseLeave={onMouseLeave}
                        >
                          {(startDate === nowDate || nowDay === 0) && el.schedule.title}
                        </div>
                      )
                    );
                  })}

                  {allDaysWithPriority.map((el) => {
                    const startDate = getISODateString(el.schedule.startDateTime);
                    const nowDate = getFormattedDate(info.year, info.month, info.date);

                    if (startDate === nowDate && el.priority >= maxView) {
                      return (
                        <span css={moreStyle} onClick={(e) => handleClickMoreButton(e, info)}>
                          일정 더보기
                        </span>
                      );
                    }

                    return (
                      startDate === nowDate && (
                        <div
                          key={`${nowDate}#${el.schedule.id}`}
                          css={itemWithBackgroundStyle(
                            el.priority,
                            el.schedule.colorCode,
                            hoveringId === el.schedule.id,
                            maxView
                          )}
                          onMouseEnter={() => onMouseEnter(el.schedule.id)}
                          onClick={(e) => handleClickSchedule(e, el.schedule)}
                          onMouseLeave={onMouseLeave}
                        >
                          {el.schedule.title}
                        </div>
                      )
                    );
                  })}

                  {fewHoursWithPriority.map((el) => {
                    const startDate = getISODateString(el.schedule.startDateTime);
                    const nowDate = getFormattedDate(info.year, info.month, info.date);

                    if (startDate === nowDate && el.priority >= maxView) {
                      return (
                        <span css={moreStyle} onClick={(e) => handleClickMoreButton(e, info)}>
                          일정 더보기
                        </span>
                      );
                    }

                    return (
                      startDate === nowDate && (
                        <div
                          key={`${nowDate}#${el.schedule.id}`}
                          css={itemWithoutBackgroundStyle(
                            theme,
                            el.priority,
                            el.schedule.colorCode,
                            hoveringId === el.schedule.id,
                            maxView
                          )}
                          onMouseEnter={() => onMouseEnter(el.schedule.id)}
                          onClick={(e) => handleClickSchedule(e, el.schedule)}
                          onMouseLeave={onMouseLeave}
                        >
                          {el.schedule.title}
                        </div>
                      )
                    );
                  })}
                </div>

                {info === moreDateInfo && (
                  <ModalPortal
                    isOpen={isDateModalOpen}
                    closeModal={toggleDateModalOpen}
                    dimmerBackground={TRANSPARENT}
                  >
                    <DateModal
                      dateModalPos={modalPos}
                      moreDateInfo={moreDateInfo}
                      longTermsWithPriority={longTermsWithPriority}
                      allDaysWithPriority={allDaysWithPriority}
                      fewHoursWithPriority={fewHoursWithPriority}
                    />
                  </ModalPortal>
                )}
              </>
            );
          })}
        </div>
      </div>

      <ModalPortal isOpen={isScheduleAddModalOpen} closeModal={toggleScheduleAddModalOpen}>
        <ScheduleAddModal dateInfo={dateInfo} closeModal={toggleScheduleAddModalOpen} />
      </ModalPortal>
      <ScheduleAddButton onClick={toggleScheduleAddModalOpen} />

      {scheduleInfo ? (
        <>
          <ModalPortal
            isOpen={isScheduleModalOpen}
            closeModal={toggleScheduleModalOpen}
            dimmerBackground={TRANSPARENT}
          >
            <ScheduleModal
              scheduleModalPos={modalPos}
              scheduleInfo={scheduleInfo}
              toggleScheduleModifyModalOpen={toggleScheduleModifyModalOpen}
              closeModal={toggleScheduleModalOpen}
            />
          </ModalPortal>
          <ModalPortal
            isOpen={isScheduleModifyModalOpen}
            closeModal={toggleScheduleModifyModalOpen}
          >
            <ScheduleModifyModal
              scheduleInfo={scheduleInfo}
              closeModal={toggleScheduleModifyModalOpen}
            />
          </ModalPortal>
        </>
      ) : (
        <></>
      )}
    </PageLayout>
  );
}

export default CalendarPage;
