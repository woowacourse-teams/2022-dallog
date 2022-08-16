import { validateLength } from '@/validation';
import { useTheme } from '@emotion/react';
import { AxiosError, AxiosResponse } from 'axios';
import { useMutation, useQueryClient } from 'react-query';
import { useRecoilValue } from 'recoil';

import useControlledInput from '@/hooks/useControlledInput';

import { CategoryType } from '@/@types/category';

import { userState } from '@/recoil/atoms';

import Button from '@/components/@common/Button/Button';
import Fieldset from '@/components/@common/Fieldset/Fieldset';
import {
  cancelButton,
  content,
  controlButtons,
  form,
  saveButton,
} from '@/components/CategoryAddModal/CategoryAddModal.styles';

import { CACHE_KEY, VALIDATION_SIZE } from '@/constants';
import { VALIDATION_MESSAGE } from '@/constants/message';

import categoryApi from '@/api/category';

import {
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

  const { inputValue, onChangeValue } = useControlledInput();

  const queryClient = useQueryClient();

  const { mutate } = useMutation<
    AxiosResponse<CategoryType>,
    AxiosError,
    Pick<CategoryType, 'name'>,
    unknown
  >((body) => categoryApi.post(accessToken, body), {
    onSuccess: () => onSuccessPostCategory(),
    useErrorBoundary: true,
  });

  const handleSubmitCategoryAddForm = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    mutate({ name: inputValue });
  };

  const onSuccessPostCategory = () => {
    queryClient.invalidateQueries(CACHE_KEY.CATEGORIES);
    queryClient.invalidateQueries(CACHE_KEY.MY_CATEGORIES);
    queryClient.invalidateQueries(CACHE_KEY.SUBSCRIPTIONS);
  };

  const googleList = ['우테코', '우테코BE', '우테코FE', '내일정'];

  return (
    <div css={layoutStyle}>
      <div css={headerStyle}>구글 캘린더 가져오기</div>
      <div css={googleSelectBoxStyle}>
        <div css={titleStyle}>구글 캘린더 목록</div>
        <select
          id="myCategories"
          defaultValue={googleList[0]}
          css={googleSelectStyle}
          // onChange={handleChangeMyCategorySelect}
        >
          <option value="" disabled>
            구글 캘린더 목록
          </option>
          {googleList.map((el) => (
            <option key={el} value={el}>
              {el}
            </option>
          ))}
        </select>
      </div>
      <form css={form} onSubmit={handleSubmitCategoryAddForm}>
        <div css={content}>
          <Fieldset
            placeholder="카테고리 이름"
            autoFocus={true}
            onChange={onChangeValue}
            errorMessage={VALIDATION_MESSAGE.STRING_LENGTH(
              VALIDATION_SIZE.MIN_LENGTH,
              VALIDATION_SIZE.CATEGORY_NAME_MAX_LENGTH
            )}
            isValid={validateLength(
              inputValue,
              VALIDATION_SIZE.MIN_LENGTH,
              VALIDATION_SIZE.CATEGORY_NAME_MAX_LENGTH
            )}
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
            disabled={
              !validateLength(
                inputValue,
                VALIDATION_SIZE.MIN_LENGTH,
                VALIDATION_SIZE.CATEGORY_NAME_MAX_LENGTH
              )
            }
          >
            완료
          </Button>
        </div>
      </form>
    </div>
  );
}

export default GoogleImportModal;
