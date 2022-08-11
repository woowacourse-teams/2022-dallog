import { useTheme } from '@emotion/react';
import { AxiosError, AxiosResponse } from 'axios';
import { useState } from 'react';
import { useQuery } from 'react-query';
import { useRecoilValue } from 'recoil';

import useCalendar from '@/hooks/useCalendar';
import useSchedulePriority from '@/hooks/useSchedulePriority';
import useToggle from '@/hooks/useToggle';

import { CalendarType } from '@/@types/calendar';
import { ScheduleResponseType, ScheduleType } from '@/@types/schedule';

import { userState } from '@/recoil/atoms';

import Button from '@/components/@common/Button/Button';
import ModalPortal from '@/components/@common/ModalPortal/ModalPortal';
import PageLayout from '@/components/@common/PageLayout/PageLayout';
import Spinner from '@/components/@common/Spinner/Spinner';
import ScheduleAddButton from '@/components/ScheduleAddButton/ScheduleAddButton';
import ScheduleAddModal from '@/components/ScheduleAddModal/ScheduleAddModal';
import ScheduleModifyModal from '@/components/ScheduleModifyModal/ScheduleModifyModal';

import { CACHE_KEY, DAYS } from '@/constants';

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
  navBarGrid,
  navButton,
  navButtonTitle,
  spinnerStyle,
  todayButton,
  waitingNavStyle,
} from './CalendarPage.styles';

function CalendarPage() {
  const theme = useTheme();

  const { accessToken } = useRecoilValue(userState);

  const [hoveringId, setHoveringId] = useState(0);
  const [dateInfo, setDateInfo] = useState<CalendarType | null>(null);
  const [scheduleInfo, setScheduleInfo] = useState<ScheduleType | null>(null);

  const {
    calendarMonth,
    current,
    moveToBeforeMonth,
    moveToToday,
    moveToNextMonth,
    startDate,
    endDate,
  } = useCalendar();

  const { getLongTermsPriority, getAllDaysPriority, getFewHoursPriority } = useSchedulePriority();

  const { state: isScheduleAddModalOpen, toggleState: toggleScheduleAddModalOpen } = useToggle();
  const { state: isScheduleModifyModalOpen, toggleState: toggleScheduleModifyModalOpen } =
    useToggle();

  const { isLoading, error, data } = useQuery<AxiosResponse<ScheduleResponseType>, AxiosError>(
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

    setScheduleInfo(info);
    toggleScheduleModifyModalOpen();
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

  if (error) {
    return (
      <PageLayout>
        <span>Error가 발생했습니다.</span>
      </PageLayout>
    );
  }

  const longTermsWithPriority = getLongTermsPriority(data.data.longTerms);
  const allDaysWithPriority = getAllDaysPriority(data.data.allDays);
  const fewHoursWithPriority = getFewHoursPriority(data.data.fewHours);

  const onMouseEnter = (scheduleId: number) => {
    setHoveringId(scheduleId);
  };

  const onMouseLeave = () => {
    setHoveringId(0);
  };

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
              <div
                key={key}
                css={dateBorder(theme, info.day)}
                onClick={(e) => handleClickDate(e, info)}
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

                  return (
                    startDate <= nowDate &&
                    nowDate <= endDate && (
                      <div
                        key={`${nowDate}#${el.schedule.id}`}
                        css={itemWithBackgroundStyle(
                          el.priority,
                          el.schedule.color,
                          hoveringId === el.schedule.id
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

                  return (
                    startDate === nowDate && (
                      <div
                        key={`${nowDate}#${el.schedule.id}`}
                        css={itemWithBackgroundStyle(
                          el.priority,
                          el.schedule.color,
                          hoveringId === el.schedule.id
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

                  return (
                    startDate === nowDate && (
                      <div
                        key={`${nowDate}#${el.schedule.id}`}
                        css={itemWithoutBackgroundStyle(
                          theme,
                          el.priority,
                          el.schedule.color,
                          hoveringId === el.schedule.id
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
            );
          })}
        </div>
        <ModalPortal isOpen={isScheduleAddModalOpen} closeModal={toggleScheduleAddModalOpen}>
          <ScheduleAddModal dateInfo={dateInfo} closeModal={toggleScheduleAddModalOpen} />
        </ModalPortal>
        <ModalPortal isOpen={isScheduleModifyModalOpen} closeModal={toggleScheduleModifyModalOpen}>
          <ScheduleModifyModal
            scheduleInfo={scheduleInfo}
            closeModal={toggleScheduleModifyModalOpen}
          />
        </ModalPortal>
        <ScheduleAddButton onClick={toggleScheduleAddModalOpen} />
      </div>
    </PageLayout>
  );
}

export default CalendarPage;
