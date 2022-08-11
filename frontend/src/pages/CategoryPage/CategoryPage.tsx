import { useTheme } from '@emotion/react';
import { lazy, Suspense, useRef, useState } from 'react';

import useToggle from '@/hooks/useToggle';

import Button from '@/components/@common/Button/Button';
import Fieldset from '@/components/@common/Fieldset/Fieldset';
import ModalPortal from '@/components/@common/ModalPortal/ModalPortal';
import PageLayout from '@/components/@common/PageLayout/PageLayout';
import CategoryAddModal from '@/components/CategoryAddModal/CategoryAddModal';
import CategoryListFallback from '@/components/CategoryList/CategoryList.fallback';
import MyCategoryListFallback from '@/components/MyCategoryList/MyCategoryList.fallback';

import { GoSearch } from 'react-icons/go';

import {
  addButtonStyle,
  categoryPageStyle,
  controlStyle,
  filteringButtonStyle,
  searchButtonStyle,
  searchFieldsetStyle,
  searchFormStyle,
  searchInputStyle,
} from './CategoryPage.styles';

const CategoryList = lazy(() => import('@/components/CategoryList/CategoryList'));
const MyCategoryList = lazy(() => import('@/components/MyCategoryList/MyCategoryList'));

function CategoryPage() {
  const theme = useTheme();
  const [mode, setMode] = useState<'ALL' | 'MY'>('ALL');
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

  const handleClickFilteringButton = () => {
    mode === 'ALL' && setMode('MY');
    mode === 'MY' && setMode('ALL');
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
              <GoSearch size={16} />
            </Button>
            <Fieldset
              placeholder={mode === 'MY' ? '검색 불가능' : '카테고리 이름 검색'}
              cssProp={{ div: searchFieldsetStyle, input: searchInputStyle }}
              refProp={keywordRef}
              disabled={mode === 'MY'}
            />
          </form>
          <Button cssProp={filteringButtonStyle(theme)} onClick={handleClickFilteringButton}>
            {mode === 'ALL' ? '나의 카테고리 보기 ' : '전체 카테고리 보기'}
          </Button>
          <Button cssProp={addButtonStyle(theme)} onClick={handleClickCategoryAddButton}>
            카테고리 추가
          </Button>
        </div>
        {mode === 'ALL' && (
          <Suspense fallback={<CategoryListFallback />}>
            <CategoryList keyword={keyword} />
          </Suspense>
        )}
        {mode === 'MY' && (
          <Suspense fallback={<MyCategoryListFallback />}>
            <MyCategoryList />
          </Suspense>
        )}
      </div>
    </PageLayout>
  );
}

export default CategoryPage;
