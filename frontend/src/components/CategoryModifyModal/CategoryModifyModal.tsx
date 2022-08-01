import { useTheme } from '@emotion/react';
import { AxiosError, AxiosResponse } from 'axios';
import { useRef } from 'react';
import { useMutation, useQueryClient } from 'react-query';
import { useRecoilValue } from 'recoil';

import { CategoryType } from '@/@types/category';

import Button from '@/components/@common/Button/Button';
import FieldSet from '@/components/@common/FieldSet/FieldSet';

import {
  cancelButton,
  content,
  controlButtons,
  form,
  modal,
  saveButton,
  title,
} from './CategoryModifyModal.styles';

interface CategoryModifyModalProps {
  category: CategoryType;
  closeModal: () => void;
}

function CategoryModifyModal({ category, closeModal }: CategoryModifyModalProps) {
  const theme = useTheme();

  const inputRef = {
    name: useRef<HTMLInputElement>(null),
  };

  const handleClickModal = (e: React.MouseEvent) => {
    e.stopPropagation();
  };

  return (
    <div css={modal} onClick={handleClickModal}>
      <h1 css={title}>카테고리 수정</h1>
      <form css={form}>
        <div css={content}>
          <FieldSet placeholder={category.name} autoFocus={true} refProp={inputRef.name} />
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

export default CategoryModifyModal;
