import Skeleton from '@/components/@common/Skeleton/Skeleton';

import { imageStyle, layoutStyle, skeletonStyle } from './Profile.styles';

function ProfileFallback() {
  return (
    <div css={layoutStyle}>
      <Skeleton cssProp={imageStyle} />
      <div css={skeletonStyle}>
        <Skeleton width="30rem" height="4rem" />
        <Skeleton width="30rem" height="4rem" />
        <Skeleton width="20rem" height="4rem" />
      </div>
    </div>
  );
}

export default ProfileFallback;
