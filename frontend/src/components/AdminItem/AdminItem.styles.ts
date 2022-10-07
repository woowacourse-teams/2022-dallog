import { css, Theme } from '@emotion/react';

const adminButtonStyle = css`
  position: absolute;
  right: 1rem;

  font-size: 5rem;
  line-height: 7rem;

  &:hover {
    transform: scale(1.1);
  }
`;

const adminItemStyle = ({ colors, flex }: Theme) => css`
  ${flex.row};

  justify-content: flex-start;
  gap: 2rem;
  position: relative;

  height: 7rem;
  padding: 6rem 2rem;
  box-shadow: 0 2px 2px ${colors.GRAY_400};

  font-size: 4rem;

  &:hover {
    background: ${colors.GRAY_100};
  }
`;

const profileImageStyle = css`
  width: 7rem;
  height: 7rem;
  border-radius: 50%;
`;

export { adminButtonStyle, adminItemStyle, profileImageStyle };
