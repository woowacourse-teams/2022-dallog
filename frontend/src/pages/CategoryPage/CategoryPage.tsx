import { useTheme } from '@emotion/react';
import { lazy, Suspense, useRef, useState } from 'react';

import useToggle from '@/hooks/useToggle';

import Button from '@/components/@common/Button/Button';
import Fieldset from '@/components/@common/Fieldset/Fieldset';
import ModalPortal from '@/components/@common/ModalPortal/ModalPortal';
import PageLayout from '@/components/@common/PageLayout/PageLayout';
import CategoryAddModal from '@/components/CategoryAddModal/CategoryAddModal';
import CategoryListFallback from '@/components/CategoryList/CategoryList.fallback';
import GoogleImportModal from '@/components/GoogleImportModal/GoogleImportModal';
import MyCategoryListFallback from '@/components/MyCategoryList/MyCategoryList.fallback';

import { GoSearch } from 'react-icons/go';

import {
  buttonStyle,
  categoryPageStyle,
  controlStyle,
  modeTextStyle,
  outLineButtonStyle,
  searchButtonStyle,
  searchFieldsetStyle,
  searchFormStyle,
  searchInputStyle,
  toggleModeStyle,
} from './CategoryPage.styles';

const CategoryList = lazy(() => import('@/components/CategoryList/CategoryList'));
const MyCategoryList = lazy(() => import('@/components/MyCategoryList/MyCategoryList'));

function CategoryPage() {
  const theme = useTheme();
  const [mode, setMode] = useState<'ALL' | 'MY'>('ALL');
  const { state: isCategoryAddModalOpen, toggleState: toggleCategoryAddModalOpen } = useToggle();
  const { state: isGoogleImportModalOpen, toggleState: toggleGoogleImportModalOpen } = useToggle();

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

  const handleClickGoogleImportButton = () => {
    toggleGoogleImportModalOpen();
  };

  return (
    <PageLayout>
      <div css={categoryPageStyle}>
        <ModalPortal isOpen={isGoogleImportModalOpen} closeModal={toggleGoogleImportModalOpen}>
          <GoogleImportModal closeModal={toggleGoogleImportModalOpen} />
        </ModalPortal>
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
          <Button cssProp={toggleModeStyle(theme, mode)} onClick={handleClickFilteringButton}>
            <span css={modeTextStyle(theme, mode === 'ALL')}>전체</span>
            <span css={modeTextStyle(theme, mode === 'MY')}>개인</span>
          </Button>

          <Button cssProp={outLineButtonStyle(theme)} onClick={handleClickGoogleImportButton}>
            <p>구글 캘린더</p>
            <p>가져오기</p>
          </Button>

          <Button cssProp={buttonStyle(theme)} onClick={handleClickCategoryAddButton}>
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
