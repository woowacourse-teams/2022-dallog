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
        <span css={itemStyle}>생성 날짜</span>
        <span css={itemStyle}>카테고리 이름</span>
        <span css={itemStyle}>생성자</span>
        <span css={itemStyle}>구독 상태</span>
      </div>
      <div css={categoryTableStyle}>
        {new Array(6).fill(0).map((el, index) => (
          <div css={categoryItem} key={index}>
            <Skeleton cssProp={item} width="25rem" height="5rem" />
            <Skeleton cssProp={item} width="35rem" height="5rem" />
            <Skeleton cssProp={item} width="20rem" height="5rem" />
            <Skeleton cssProp={item} width="15rem" height="5rem" />
          </div>
        ))}
      </div>
    </div>
  );
}

export default CategoryListFallback;
