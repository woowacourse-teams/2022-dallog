import { SerializedStyles, useTheme } from '@emotion/react';

import { skeletonStyle } from './Skeleton.styles';

interface SkeletonProps {
  cssProp?: SerializedStyles;
  width?: string;
  height?: string;
}

function Skeleton({ cssProp, width = '100%', height = '100%' }: SkeletonProps) {
  const theme = useTheme();

  return (
    <div css={cssProp}>
      <div css={skeletonStyle(theme, width, height)}></div>
    </div>
  );
}

export default Skeleton;
