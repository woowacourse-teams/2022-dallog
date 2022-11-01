import { useTheme } from '@emotion/react';
import { useRecoilState } from 'recoil';

import useModalPosition from '@/hooks/useModalPosition';
import useToggle from '@/hooks/useToggle';

import { ScheduleResponseKeyType, ScheduleType } from '@/@types/schedule';

import { scheduleState } from '@/recoil/atoms';

import { CALENDAR } from '@/constants';
import { SCHEDULE } from '@/constants/schedule';
import { TRANSPARENT } from '@/constants/style';

import ModalPortal from '../@common/ModalPortal/ModalPortal';
import ScheduleModal from '../ScheduleModal/ScheduleModal';
import ScheduleModifyModal from '../ScheduleModifyModal/ScheduleModifyModal';
import { itemWithBackgroundStyle, itemWithoutBackgroundStyle } from './Schedule.styles';

interface ScheduleProps {
  type: ScheduleResponseKeyType;
  schedule: ScheduleType;
  priority: number;
  maxScheduleCount: number;
  isEndDate: boolean;
  isTitleVisible?: boolean;
  readonly?: boolean;
}

function Schedule({
  type,
  schedule,
  priority,
  maxScheduleCount,
  isEndDate,
  isTitleVisible = true,
  readonly = false,
}: ScheduleProps) {
  const theme = useTheme();

  const [hoveringId, setHoveringId] = useRecoilState(scheduleState);

  const { state: isScheduleModifyModalOpen, toggleState: toggleScheduleModifyModalOpen } =
    useToggle();

  const scheduleModal = useModalPosition();

  const handleMouseEnterSchedule = (scheduleId: string) => {
    setHoveringId(scheduleId);
  };

  const handleMouseLeaveSchedule = () => {
    setHoveringId('');
  };

  return (
    <div>
      <div
        css={
          type === SCHEDULE.RESPONSE_TYPE.FEW_HOURS
            ? itemWithoutBackgroundStyle(
                theme,
                priority,
                maxScheduleCount,
                isEndDate,
                hoveringId === schedule.id,
                readonly ? '' : schedule.colorCode
              )
            : itemWithBackgroundStyle(
                theme,
                priority,
                maxScheduleCount,
                isEndDate,
                hoveringId === schedule.id,
                readonly ? '' : schedule.colorCode
              )
        }
        onMouseEnter={() => handleMouseEnterSchedule(schedule.id)}
        onClick={scheduleModal.handleClickOpen}
        onMouseLeave={handleMouseLeaveSchedule}
      >
        {isTitleVisible && (schedule.title.trim() || CALENDAR.EMPTY_TITLE)}
      </div>

      <ModalPortal
        isOpen={scheduleModal.isModalOpen}
        closeModal={scheduleModal.toggleModalOpen}
        dimmerBackground={TRANSPARENT}
      >
        <ScheduleModal
          scheduleModalPos={scheduleModal.modalPos}
          scheduleInfo={schedule}
          toggleScheduleModifyModalOpen={toggleScheduleModifyModalOpen}
          closeModal={scheduleModal.toggleModalOpen}
          readonly={readonly}
        />
      </ModalPortal>

      {!readonly && (
        <ModalPortal isOpen={isScheduleModifyModalOpen} closeModal={toggleScheduleModifyModalOpen}>
          <ScheduleModifyModal scheduleInfo={schedule} closeModal={toggleScheduleModifyModalOpen} />
        </ModalPortal>
      )}
    </div>
  );
}

export default Schedule;
