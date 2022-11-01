import { lazy, memo, Suspense, useRef, useState } from 'react';

import useRootFontSize from '@/hooks/useRootFontSize';
import useToggle from '@/hooks/useToggle';

import { CategoryType } from '@/@types/category';

import theme from '@/styles/theme';

import Button from '@/components/@common/Button/Button';
import Fieldset from '@/components/@common/Fieldset/Fieldset';
import ModalPortal from '@/components/@common/ModalPortal/ModalPortal';
import CategoryAddModal from '@/components/CategoryAddModal/CategoryAddModal';
import CategoryListFallback from '@/components/CategoryList/CategoryList.fallback';

import { MdSearch } from 'react-icons/md';

import {
  buttonStyle,
  categoryHeaderStyle,
  categoryStyle,
  controlStyle,
  searchButtonStyle,
  searchFieldsetStyle,
  searchFormStyle,
  searchInputStyle,
} from './CategoryCotrol.styles';

const CategoryList = lazy(() => import('@/components/CategoryList/CategoryList'));

interface CategoryControlProps {
  setCategory: React.Dispatch<React.SetStateAction<Pick<CategoryType, 'id' | 'name'>>>;
}

function CategoryControl({ setCategory }: CategoryControlProps) {
  const keywordRef = useRef<HTMLInputElement>(null);

  const [keyword, setKeyword] = useState('');

  const rootFontSize = useRootFontSize();

  const { state: isCategoryAddModalOpen, toggleState: toggleCategoryAddModalOpen } = useToggle();

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
    <div css={categoryStyle}>
      <ModalPortal isOpen={isCategoryAddModalOpen} closeModal={toggleCategoryAddModalOpen}>
        <CategoryAddModal closeModal={toggleCategoryAddModalOpen} />
      </ModalPortal>
      <h1 css={categoryHeaderStyle}>카테고리</h1>
      <div css={controlStyle}>
        <form css={searchFormStyle} onSubmit={handleSubmitCategorySearchForm}>
          <Button type="submit" cssProp={searchButtonStyle}>
            <MdSearch size={rootFontSize * 5} />
          </Button>
          <Fieldset
            placeholder="제목 찾기"
            cssProp={{ div: searchFieldsetStyle, input: searchInputStyle }}
            refProp={keywordRef}
          />
        </form>
        <Button cssProp={buttonStyle(theme)} onClick={handleClickCategoryAddButton}>
          추가
        </Button>
      </div>
      <Suspense fallback={<CategoryListFallback />}>
        <CategoryList keyword={keyword} setCategory={setCategory} />
      </Suspense>
    </div>
  );
}

export default memo(CategoryControl);
