import Skeleton from '@/components/@common/Skeleton/Skeleton';
import {
  categoryTableHeaderStyle,
  categoryTableStyle,
  itemStyle,
} from '@/components/CategoryList/CategoryList.styles';
import {
  categoryItem,
  item,
} from '@/components/SubscribedCategoryItem/SubscribedCategoryItem.styles';

function CategoryListFallback() {
  return (
    <div>
      <div css={categoryTableHeaderStyle}>
        <span css={itemStyle}>생성 날짜</span>
        <span css={itemStyle}>카테고리 이름</span>
        <span css={itemStyle}>수정 / 삭제</span>
      </div>
      <div css={categoryTableStyle}>
        {new Array(6).fill(0).map((el, index) => (
          <div css={categoryItem} key={index}>
            <Skeleton cssProp={item} width="30rem" height="5rem" />
            <Skeleton cssProp={item} width="30rem" height="5rem" />
            <Skeleton cssProp={item} width="20rem" height="5rem" />
          </div>
        ))}
      </div>
    </div>
  );
}

export default CategoryListFallback;
