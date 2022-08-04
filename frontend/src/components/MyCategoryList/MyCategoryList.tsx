import { useTheme } from '@emotion/react';
import { AxiosError, AxiosResponse } from 'axios';
import { useQuery } from 'react-query';
import { useRecoilValue } from 'recoil';

import useToggle from '@/hooks/useToggle';

import { CategoryType } from '@/@types/category';

import { sideBarState, userState } from '@/recoil/atoms';

import Button from '@/components/@common/Button/Button';
import ModalPortal from '@/components/@common/ModalPortal/ModalPortal';
import Spinner from '@/components/@common/Spinner/Spinner';
import CategoryAddModal from '@/components/CategoryAddModal/CategoryAddModal';
import MyCategoryItem from '@/components/MyCategoryItem/MyCategoryItem';

import { CACHE_KEY } from '@/constants';

import categoryApi from '@/api/category';

import { FiPlus } from 'react-icons/fi';

import {
  buttonStyle,
  contentStyle,
  headerStyle,
  listStyle,
  spinnerStyle,
} from './MyCategoryList.styles';

function MyCategoryList() {
  const theme = useTheme();
  const { accessToken } = useRecoilValue(userState);
  const isSideBarOpen = useRecoilValue(sideBarState);

  const { state: isCategoryAddModalOpen, toggleState: toggleCategoryAddModalOpen } = useToggle();

  const { isLoading, data } = useQuery<AxiosResponse<CategoryType[]>, AxiosError>(
    CACHE_KEY.MY_CATEGORIES,
    () => categoryApi.getMy(accessToken)
  );

  if (isLoading || data === undefined) {
    return (
      <div css={spinnerStyle}>
        <Spinner size={10} />
      </div>
    );
  }

  return (
    <div css={listStyle(theme, isSideBarOpen)}>
      <div css={headerStyle}>
        <ModalPortal isOpen={isCategoryAddModalOpen} closeModal={toggleCategoryAddModalOpen}>
          <CategoryAddModal closeModal={toggleCategoryAddModalOpen} />
        </ModalPortal>
        <span>나의 카테고리</span>
        <div>
          <Button cssProp={buttonStyle} onClick={toggleCategoryAddModalOpen}>
            <FiPlus size={20} />
          </Button>
        </div>
      </div>
      <div css={contentStyle}>
        {data.data.map((category) => (
          <MyCategoryItem key={category.id} category={category} />
        ))}
      </div>
    </div>
  );
}

export default MyCategoryList;
