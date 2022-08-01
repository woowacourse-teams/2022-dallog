import Button from '@/components/@common/Button/Button';
import Fieldset from '@/components/@common/Fieldset/Fieldset';
import PageLayout from '@/components/@common/PageLayout/PageLayout';
import CategoryList from '@/components/CategoryList/CategoryList';

import { GoSearch } from 'react-icons/go';

import {
  categoryPage,
  searchButton,
  searchFieldset,
  searchForm,
  searchInput,
} from './CategoryPage.styles';

function CategoryPage() {
  const handleSubmitCategorySearchForm = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
  };

  return (
    <PageLayout>
      <div css={categoryPage}>
        <form css={searchForm} onSubmit={handleSubmitCategorySearchForm}>
          <Button type="submit" cssProp={searchButton}>
            <GoSearch size={16} />
          </Button>
          <Fieldset
            placeholder="카테고리 이름 검색"
            cssProp={{ div: searchFieldset, input: searchInput }}
          />
        </form>
        <CategoryList />
      </div>
    </PageLayout>
  );
}

export default CategoryPage;
