import Skeleton from '@/components/@common/Skeleton/Skeleton';
import {
  categoryItem,
  item,
} from '@/components/SubscribedCategoryItem/SubscribedCategoryItem.styles';

import { categoryTableHeaderStyle, categoryTableStyle, itemStyle } from './CategoryList.styles';

function CategoryListFallback() {
  return (
    <div>
      <div css={categoryTableHeaderStyle}>
        <span css={itemStyle}>제목</span>
        <span css={itemStyle}>개설자</span>
        <span css={itemStyle}>구독</span>
      </div>
      <div css={categoryTableStyle}>
        {new Array(10).fill(0).map((el, index) => (
          <div css={categoryItem} key={index}>
            <Skeleton cssProp={item} width="60%" height="6rem" />
            <Skeleton cssProp={item} width="60%" height="6rem" />
            <Skeleton cssProp={item} width="60%" height="6rem" />
          </div>
        ))}
      </div>
    </div>
  );
}

export default CategoryListFallback;
