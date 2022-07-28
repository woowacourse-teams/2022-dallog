import { useTheme } from '@emotion/react';
import { AxiosError, AxiosResponse } from 'axios';
import { useQuery } from 'react-query';
import { useRecoilValue } from 'recoil';

import useToggle from '@/hooks/useToggle';

import { CategoryType } from '@/@types/category';

import { sideBarState, userState } from '@/recoil/atoms';

import Button from '@/components/@common/Button/Button';
import ModalPortal from '@/components/@common/ModalPortal/ModalPortal';
import CategoryAddModal from '@/components/CategoryAddModal/CategoryAddModal';

import { CACHE_KEY } from '@/constants';

import categoryApi from '@/api/category';

import { FiEdit3, FiPlus } from 'react-icons/fi';
import { RiDeleteBin6Line } from 'react-icons/ri';

import {
  buttonStyle,
  contentStyle,
  controlButtonsStyle,
  headerStyle,
  itemStyle,
  listStyle,
  nameStyle,
} from './MyCategoryList.styles';

function MyCategoryList() {
  const theme = useTheme();
  const { accessToken } = useRecoilValue(userState);
  const isSideBarOpen = useRecoilValue(sideBarState);

  const { state: isCategoryAddModalOpen, toggleState: toggleCategoryAddModalOpen } = useToggle();

  const { data } = useQuery<AxiosResponse<CategoryType[]>, AxiosError>(
    CACHE_KEY.MY_CATEGORIES,
    () => categoryApi.getMy(accessToken)
  );

  if (data === undefined) {
    return <div>Loading</div>;
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
          <div key={category.id} css={itemStyle}>
            <span css={nameStyle}>{category.name}</span>
            <div css={controlButtonsStyle}>
              <Button cssProp={buttonStyle}>
                <FiEdit3 size={20} />
              </Button>
              <Button cssProp={buttonStyle}>
                <RiDeleteBin6Line size={20} />
              </Button>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}

export default MyCategoryList;
