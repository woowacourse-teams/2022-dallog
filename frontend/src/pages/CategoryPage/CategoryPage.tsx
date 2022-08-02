import { useRef, useState } from 'react';

import Button from '@/components/@common/Button/Button';
import Fieldset from '@/components/@common/Fieldset/Fieldset';
import PageLayout from '@/components/@common/PageLayout/PageLayout';
import CategoryList from '@/components/CategoryList/CategoryList';

import { GoSearch } from 'react-icons/go';

import {
  categoryPageStyle,
  searchButtonStyle,
  searchFieldsetStyle,
  searchFormStyle,
  searchInputStyle,
} from './CategoryPage.styles';

function CategoryPage() {
  const keywordRef = useRef<HTMLInputElement>(null);
  const [keyword, setKeyword] = useState('');

  const handleSubmitCategorySearchForm = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    if (!(keywordRef.current instanceof HTMLInputElement)) {
      return;
    }

    setKeyword((keywordRef.current as HTMLInputElement).value);
  };

  return (
    <PageLayout>
      <div css={categoryPageStyle}>
        <form css={searchFormStyle} onSubmit={handleSubmitCategorySearchForm}>
          <Button type="submit" cssProp={searchButtonStyle}>
            <GoSearch size={16} />
          </Button>
          <Fieldset
            placeholder="카테고리 이름 검색"
            cssProp={{ div: searchFieldsetStyle, input: searchInputStyle }}
            refProp={keywordRef}
          />
        </form>
        <CategoryList keyword={keyword} />
      </div>
    </PageLayout>
  );
}

export default CategoryPage;
