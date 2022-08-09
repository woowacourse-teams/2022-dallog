import { AxiosError, AxiosResponse } from 'axios';
import { useQuery } from 'react-query';
import { useRecoilValue } from 'recoil';

import { CategoryType } from '@/@types/category';

import { userState } from '@/recoil/atoms';

import Spinner from '@/components/@common/Spinner/Spinner';
import MyCategoryItem from '@/components/MyCategoryItem/MyCategoryItem';

import { CACHE_KEY } from '@/constants';

import categoryApi from '@/api/category';

import { headerStyle, itemStyle, listStyle, spinnerStyle } from './MyCategoryList.styles';

function MyCategoryList() {
  const { accessToken } = useRecoilValue(userState);

  const { isLoading, data } = useQuery<AxiosResponse<CategoryType[]>, AxiosError>(
    CACHE_KEY.MY_CATEGORIES,
    () => categoryApi.getMy(accessToken)
  );

  if (isLoading || data === undefined) {
    return (
      <div css={spinnerStyle}>
        <Spinner size={10} />
      </div>
    );
  }

  return (
    <>
      <div css={headerStyle}>
        <span css={itemStyle}>생성 날짜</span>
        <span css={itemStyle}>카테고리 이름</span>
        <span css={itemStyle}>수정 / 삭제</span>
      </div>
      <div css={listStyle}>
        {data.data.map((category) => (
          <MyCategoryItem key={category.id} category={category} />
        ))}
      </div>
    </>
  );
}

export default MyCategoryList;
