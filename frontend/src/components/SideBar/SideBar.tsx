import { useTheme } from '@emotion/react';

import useToggle from '@/hooks/useToggle';

import Button from '@/components/@common/Button/Button';
import ModalPortal from '@/components/@common/ModalPortal/ModalPortal';
import CategoryAddModal from '@/components/CategoryAddModal/CategoryAddModal';

import { FiEdit3, FiPlus } from 'react-icons/fi';
import { RiDeleteBin6Line } from 'react-icons/ri';

import { button, list, myCategory, sideBar, title } from './SideBar.styles';

interface SideBarProps {
  isOpen: boolean;
}

function SideBar({ isOpen }: SideBarProps) {
  const theme = useTheme();
  const { state: isCategoryAddModalOpen, toggleState: toggleCategoryAddModalOpen } = useToggle();

  return (
    <div css={sideBar(theme, isOpen)}>
      <div css={list(theme, isOpen)}>
        <div css={title}>
          <ModalPortal isOpen={isCategoryAddModalOpen} closeModal={toggleCategoryAddModalOpen}>
            <CategoryAddModal closeModal={toggleCategoryAddModalOpen} />
          </ModalPortal>
          <span>나의 카테고리</span>
          <Button cssProp={button} onClick={toggleCategoryAddModalOpen}>
            <FiPlus size={20} />
          </Button>
        </div>
        <div css={myCategory}>
          <span>우아한테크코스 FE</span>
          <div>
            <Button cssProp={button}>
              <FiEdit3 size={20} />
            </Button>
            <Button cssProp={button}>
              <RiDeleteBin6Line size={20} />
            </Button>
          </div>
        </div>
      </div>
    </div>
  );
}

export default SideBar;
