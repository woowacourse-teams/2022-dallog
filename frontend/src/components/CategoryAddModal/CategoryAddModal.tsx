import { useTheme } from '@emotion/react';
import { AxiosError, AxiosResponse } from 'axios';
import { useRef } from 'react';
import { useMutation, useQueryClient } from 'react-query';
import { useRecoilValue } from 'recoil';

import { CategoryType } from '@/@types/category';

import { userState } from '@/recoil/atoms';

import Button from '@/components/@common/Button/Button';
import FieldSet from '@/components/@common/FieldSet/FieldSet';

import { CACHE_KEY } from '@/constants';

import { createPostBody } from '@/utils';

import categoryApi from '@/api/category';

import {
  cancelButton,
  categoryAddModal,
  content,
  controlButtons,
  form,
  saveButton,
  title,
} from './CategoryAddModal.styles';

interface CategoryAddModalProps {
  closeModal: () => void;
}

function CategoryAddModal({ closeModal }: CategoryAddModalProps) {
  const theme = useTheme();

  const { accessToken } = useRecoilValue(userState);

  const queryClient = useQueryClient();
  const { mutate: postCategory } = useMutation<
    AxiosResponse<Pick<CategoryType, 'name'>>,
    AxiosError,
    Pick<CategoryType, 'name'>,
    unknown
  >((body) => categoryApi.post(accessToken, body), { onSuccess: () => onSuccessPostCategory() });

  const inputRef = {
    name: useRef<HTMLInputElement>(null),
  };

  const handleClickLoginModal = (e: React.MouseEvent) => {
    e.stopPropagation();
  };

  const handleSubmitCategoryAddForm = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    const body = createPostBody(inputRef);

    if (!body) {
      return;
    }

    postCategory(body);
  };

  const onSuccessPostCategory = () => {
    queryClient.invalidateQueries(CACHE_KEY.CATEGORIES);
    queryClient.invalidateQueries(CACHE_KEY.MY_CATEGORIES);
    closeModal();
  };

  return (
    <div css={categoryAddModal} onClick={handleClickLoginModal}>
      <h1 css={title}>새 카테고리 만들기</h1>
      <form css={form} onSubmit={handleSubmitCategoryAddForm}>
        <div css={content}>
          <FieldSet placeholder="이름" autoFocus={true} refProp={inputRef.name} />
        </div>
        <div css={controlButtons}>
          <Button cssProp={cancelButton(theme)} onClick={closeModal}>
            취소
          </Button>
          <Button type="submit" cssProp={saveButton(theme)}>
            완료
          </Button>
        </div>
      </form>
    </div>
  );
}

export default CategoryAddModal;
