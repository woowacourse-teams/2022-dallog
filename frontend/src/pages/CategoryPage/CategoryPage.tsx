import { useTheme } from '@emotion/react';
import { lazy, Suspense, useRef, useState } from 'react';

import useToggle from '@/hooks/useToggle';

import Button from '@/components/@common/Button/Button';
import Fieldset from '@/components/@common/Fieldset/Fieldset';
import ModalPortal from '@/components/@common/ModalPortal/ModalPortal';
import PageLayout from '@/components/@common/PageLayout/PageLayout';
import CategoryAddModal from '@/components/CategoryAddModal/CategoryAddModal';
import CategoryListFallback from '@/components/CategoryList/CategoryList.fallback';

import { MdSearch } from 'react-icons/md';

import {
  buttonStyle,
  categoryPageStyle,
  controlStyle,
  searchButtonStyle,
  searchFieldsetStyle,
  searchFormStyle,
  searchInputStyle,
} from './CategoryPage.styles';

const CategoryList = lazy(() => import('@/components/CategoryList/CategoryList'));

function CategoryPage() {
  const theme = useTheme();
  const { state: isCategoryAddModalOpen, toggleState: toggleCategoryAddModalOpen } = useToggle();

  const keywordRef = useRef<HTMLInputElement>(null);

  const [keyword, setKeyword] = useState('');

  const handleSubmitCategorySearchForm = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    if (!(keywordRef.current instanceof HTMLInputElement)) {
      return;
    }

    setKeyword((keywordRef.current as HTMLInputElement).value);
  };

  const handleClickCategoryAddButton = () => {
    toggleCategoryAddModalOpen();
  };

  return (
    <PageLayout>
      <div css={categoryPageStyle}>
        <ModalPortal isOpen={isCategoryAddModalOpen} closeModal={toggleCategoryAddModalOpen}>
          <CategoryAddModal closeModal={toggleCategoryAddModalOpen} />
        </ModalPortal>
        <div css={controlStyle}>
          <form css={searchFormStyle} onSubmit={handleSubmitCategorySearchForm}>
            <Button type="submit" cssProp={searchButtonStyle}>
              <MdSearch size={16} />
            </Button>
            <Fieldset
              placeholder="카테고리 이름 검색"
              cssProp={{ div: searchFieldsetStyle, input: searchInputStyle }}
              refProp={keywordRef}
            />
          </form>
          <Button cssProp={buttonStyle(theme)} onClick={handleClickCategoryAddButton}>
            카테고리 추가
          </Button>
        </div>
        <Suspense fallback={<CategoryListFallback />}>
          <CategoryList keyword={keyword} />
        </Suspense>
      </div>
    </PageLayout>
  );
}

export default CategoryPage;
