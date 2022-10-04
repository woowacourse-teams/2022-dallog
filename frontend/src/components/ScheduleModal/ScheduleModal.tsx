import { useTheme } from '@emotion/react';
import { AxiosError, AxiosResponse } from 'axios';
import { useQuery } from 'react-query';

import { useDeleteSchedule } from '@/hooks/@queries/schedule';
import useUserValue from '@/hooks/useUserValue';

import { ModalPosType } from '@/@types';
import { CategoryType } from '@/@types/category';
import { ScheduleType } from '@/@types/schedule';

import Button from '@/components/@common/Button/Button';

import { CACHE_KEY } from '@/constants/api';
import { CATEGORY_TYPE } from '@/constants/category';
import { CONFIRM_MESSAGE } from '@/constants/message';

import categoryApi from '@/api/category';

import {
  MdClose,
  MdDeleteOutline,
  MdOutlineCalendarToday,
  MdOutlineModeEdit,
} from 'react-icons/md';

import {
  buttonStyle,
  buttonTitleStyle,
  colorStyle,
  contentBlockStyle,
  contentStyle,
  grayTextStyle,
  headerStyle,
  scheduleIconStyle,
  scheduleInfoStyle,
  scheduleModalStyle,
  scheduleTitleStyle,
} from './ScheduleModal.styles';

interface ScheduleModalProps {
  scheduleModalPos: ModalPosType;
  scheduleInfo: ScheduleType;
  toggleScheduleModifyModalOpen: () => void;
  closeModal: () => void;
}

function ScheduleModal({
  scheduleModalPos,
  scheduleInfo,
  toggleScheduleModifyModalOpen,
  closeModal,
}: ScheduleModalProps) {
  const { user } = useUserValue();

  const theme = useTheme();

  const { data: categoryGetResponse } = useQuery<AxiosResponse<CategoryType>, AxiosError>(
    CACHE_KEY.CATEGORY,
    () => categoryApi.getSingle(scheduleInfo.categoryId)
  );

  const { mutate } = useDeleteSchedule({
    scheduleId: scheduleInfo.id,
    onSuccess: () => closeModal(),
  });

  const handleClickModifyButton = () => {
    closeModal();
    toggleScheduleModifyModalOpen();
  };

  const handleClickDeleteButton = () => {
    if (confirm(CONFIRM_MESSAGE.DELETE)) {
      mutate();
    }
  };

  const formatDateTime = (dateTime: string | undefined) => {
    if (dateTime === undefined) {
      return;
    }

    return dateTime.replace('T', ' ');
  };

  const canEditSchedule =
    (scheduleInfo.categoryType === CATEGORY_TYPE.NORMAL ||
      scheduleInfo.categoryType === CATEGORY_TYPE.PERSONAL) &&
    user.id === categoryGetResponse?.data.creator.id;

  return (
    <div css={scheduleModalStyle(theme, scheduleModalPos)}>
      <div css={headerStyle}>
        {canEditSchedule && (
          <>
            <Button cssProp={buttonStyle} onClick={handleClickModifyButton}>
              <MdOutlineModeEdit />
              <span css={buttonTitleStyle}>일정 수정</span>
            </Button>
            <Button cssProp={buttonStyle} onClick={handleClickDeleteButton}>
              <MdDeleteOutline />
              <span css={buttonTitleStyle}>일정 삭제</span>
            </Button>
          </>
        )}
        <Button cssProp={buttonStyle} onClick={closeModal}>
          <MdClose />
          <span css={buttonTitleStyle}>닫기</span>
        </Button>
      </div>
      <div css={contentStyle}>
        <div css={contentBlockStyle}>
          <MdOutlineCalendarToday css={scheduleIconStyle} />
          <div css={scheduleInfoStyle}>
            <p css={scheduleTitleStyle}>{scheduleInfo.title}</p>
            <p>
              <span>{formatDateTime(scheduleInfo.startDateTime)}</span>
              &nbsp;&nbsp;→&nbsp;&nbsp;
              <span>{formatDateTime(scheduleInfo.endDateTime)}</span>
            </p>
            {scheduleInfo.memo && <p>{scheduleInfo.memo}</p>}
          </div>
        </div>
        <div css={contentBlockStyle}>
          <div css={colorStyle(scheduleInfo.colorCode)} />
          <span>
            {categoryGetResponse?.data.name}
            <span css={grayTextStyle}>
              {scheduleInfo.categoryType === CATEGORY_TYPE.GOOGLE && ' (구글)'}
              {scheduleInfo.categoryType === CATEGORY_TYPE.PERSONAL && ' (기본)'}
            </span>
          </span>
        </div>
      </div>
    </div>
  );
}

export default ScheduleModal;
