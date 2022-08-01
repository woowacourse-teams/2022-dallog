import Fieldset from '@/components/@common/Fieldset/Fieldset';
import PageLayout from '@/components/@common/PageLayout/PageLayout';
import CategoryList from '@/components/CategoryList/CategoryList';

import { categoryNav, categoryPage, categorySearch } from './CategoryPage.styles';

function CategoryPage() {
  return (
    <PageLayout>
      <div css={categoryPage}>
        <div css={categoryNav}>
          <Fieldset placeholder="카테고리 검색" cssProp={categorySearch} />
        </div>
        <CategoryList />
      </div>
    </PageLayout>
  );
}

export default CategoryPage;
