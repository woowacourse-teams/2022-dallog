import { useTheme } from '@emotion/react';
import { AxiosError, AxiosResponse } from 'axios';
import { useMutation, useQuery } from 'react-query';
import { useRecoilValue } from 'recoil';

import { CategoryType } from '@/@types/category';
import { ProfileType } from '@/@types/profile';
import { ScheduleModalPosType, ScheduleType } from '@/@types/schedule';

import { userState } from '@/recoil/atoms';

import Button from '@/components/@common/Button/Button';

import { CACHE_KEY } from '@/constants';
import { CONFIRM_MESSAGE } from '@/constants/message';

import categoryApi from '@/api/category';
import profileApi from '@/api/profile';
import scheduleApi from '@/api/schedule';

import { FiCalendar, FiEdit3 } from 'react-icons/fi';
import { GrClose } from 'react-icons/gr';
import { RiDeleteBin6Line } from 'react-icons/ri';

import {
  buttonStyle,
  buttonTitleStyle,
  colorStyle,
  contentBlockStyle,
  contentStyle,
  headerStyle,
  scheduleIconStyle,
  scheduleInfoStyle,
  scheduleModalStyle,
  scheduleTitleStyle,
} from './ScheduleModal.styles';

interface ScheduleModalProps {
  scheduleModalPos: ScheduleModalPosType;
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
  const { accessToken } = useRecoilValue(userState);
  const theme = useTheme();

  const { data: profileGetResponse } = useQuery<AxiosResponse<ProfileType>, AxiosError>(
    CACHE_KEY.PROFILE,
    () => profileApi.get(accessToken)
  );

  const { data: categoryGetResponse } = useQuery<AxiosResponse<CategoryType>, AxiosError>(
    CACHE_KEY.CATEGORY,
    () => categoryApi.getSingle(scheduleInfo.categoryId)
  );

  const { mutate } = useMutation<AxiosResponse, AxiosError>(
    () => scheduleApi.delete(accessToken, scheduleInfo.id),
    {
      onSuccess: () => onSuccessDeleteSchedule(),
    }
  );

  const onSuccessDeleteSchedule = () => {
    closeModal();
  };

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

  return (
    <div css={scheduleModalStyle(theme, scheduleModalPos)}>
      <div css={headerStyle}>
        {profileGetResponse?.data.id === categoryGetResponse?.data.creator.id && (
          <>
            <Button cssProp={buttonStyle} onClick={handleClickModifyButton}>
              <FiEdit3 />
              <span css={buttonTitleStyle}>일정 수정</span>
            </Button>
            <Button cssProp={buttonStyle} onClick={handleClickDeleteButton}>
              <RiDeleteBin6Line />
              <span css={buttonTitleStyle}>일정 삭제</span>
            </Button>
          </>
        )}
        <Button cssProp={buttonStyle} onClick={closeModal}>
          <GrClose />
          <span css={buttonTitleStyle}>닫기</span>
        </Button>
      </div>
      <div css={contentStyle}>
        <div css={contentBlockStyle}>
          <FiCalendar css={scheduleIconStyle} />
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
          <p>{categoryGetResponse?.data.name}</p>
        </div>
      </div>
    </div>
  );
}

export default ScheduleModal;
