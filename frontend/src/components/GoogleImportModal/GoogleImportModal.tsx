import { validateNotEmpty } from '@/validation';
import { useTheme } from '@emotion/react';
import { AxiosError, AxiosResponse } from 'axios';
import { useMutation, useQuery, useQueryClient } from 'react-query';
import { useRecoilValue } from 'recoil';

import useControlledInput from '@/hooks/useControlledInput';
import useValidateCategory from '@/hooks/useValidateCategory';

import { GoogleCalendarGetResponseType, GoogleCalendarPostBodyType } from '@/@types/googleCalendar';

import { userState } from '@/recoil/atoms';

import Button from '@/components/@common/Button/Button';
import Fieldset from '@/components/@common/Fieldset/Fieldset';
import Spinner from '@/components/@common/Spinner/Spinner';
import {
  cancelButton,
  content,
  controlButtons,
  saveButton,
} from '@/components/CategoryAddModal/CategoryAddModal.styles';

import { CACHE_KEY } from '@/constants';

import googleCalendarApi from '@/api/googleCalendar';

import {
  formStyle,
  googleSelectBoxStyle,
  googleSelectStyle,
  headerStyle,
  inputStyle,
  layoutStyle,
  titleStyle,
} from './GoogleImportModal.styles';

interface GoogleImportModal {
  closeModal: () => void;
}

function GoogleImportModal({ closeModal }: GoogleImportModal) {
  const theme = useTheme();

  const { accessToken } = useRecoilValue(userState);

  const { categoryValue, getCategoryErrorMessage, isValidCategory } = useValidateCategory();

  const { inputValue: googleCalendarInputValue, onChangeValue: onChangeGoogleCalendarInputValue } =
    useControlledInput();

  const queryClient = useQueryClient();

  const { isLoading, data } = useQuery<AxiosResponse<GoogleCalendarGetResponseType>, AxiosError>(
    CACHE_KEY.GOOGLE_CALENDAR,
    () => googleCalendarApi.get(accessToken)
  );

  const { mutate } = useMutation(
    (body: GoogleCalendarPostBodyType) => googleCalendarApi.post(accessToken, body),
    {
      onSuccess: () => onSuccessPostCategory(),
    }
  );

  const handleSubmitCategoryAddForm = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    mutate({ externalId: googleCalendarInputValue, name: categoryValue.inputValue });
  };

  const onSuccessPostCategory = () => {
    queryClient.invalidateQueries(CACHE_KEY.CATEGORIES);
    queryClient.invalidateQueries(CACHE_KEY.MY_CATEGORIES);
    queryClient.invalidateQueries(CACHE_KEY.SUBSCRIPTIONS);

    closeModal();
  };

  if (isLoading || data === undefined) {
    return <Spinner size={10} />;
  }

  return (
    <div css={layoutStyle}>
      <div css={headerStyle}>구글 캘린더 가져오기</div>
      <form css={formStyle} onSubmit={handleSubmitCategoryAddForm}>
        <div css={googleSelectBoxStyle}>
          <div css={titleStyle}>구글 캘린더 목록</div>
          <select
            value={googleCalendarInputValue}
            css={googleSelectStyle}
            onChange={onChangeGoogleCalendarInputValue}
          >
            <option value="" disabled>
              구글 캘린더 선택 (필수)
            </option>
            {data.data.externalCalendars.map((el) => (
              <option key={el.calendarId} value={el.calendarId}>
                {el.summary}
              </option>
            ))}
          </select>
        </div>

        <div css={content}>
          <Fieldset
            placeholder="카테고리 이름"
            autoFocus={true}
            onChange={categoryValue.onChangeValue}
            errorMessage={getCategoryErrorMessage()}
            isValid={isValidCategory}
            labelText={'연동할 달록 카테고리 생성'}
            cssProp={inputStyle}
          />
        </div>
        <div css={controlButtons}>
          <Button cssProp={cancelButton(theme)} onClick={closeModal}>
            취소
          </Button>
          <Button
            type="submit"
            cssProp={saveButton(theme)}
            disabled={!isValidCategory || !validateNotEmpty(googleCalendarInputValue)}
          >
            완료
          </Button>
        </div>
      </form>
    </div>
  );
}

export default GoogleImportModal;
