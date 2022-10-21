import { css, Theme } from '@emotion/react';

const headerStyle = css`
  width: 100%;

  padding: 0 auto;

  font-size: 8rem;
  font-weight: bold;
  text-align: center;
`;

const layoutStyle = ({ colors, flex }: Theme) => css`
  ${flex.column};
  align-items: flex-start;

  gap: 6rem;

  width: 100rem;
  padding: 12.5rem;
  border-radius: 7px;

  font-size: 4rem;
  line-height: 6rem;

  background: ${colors.WHITE};
`;

const withdrawalButtonStyle = ({ colors }: Theme) => css`
  width: 80%;
  margin: 0 auto;

  padding: 2rem 3rem;
  border: 1px solid ${colors.RED_400};
  border-radius: 7px;

  font-size: 3rem;
  color: ${colors.RED_400};
`;

const withdrawalConditionTextStyle = ({ colors }: Theme) => css`
  color: ${colors.RED_400};
  font-weight: 700;
`;

export { headerStyle, layoutStyle, withdrawalButtonStyle, withdrawalConditionTextStyle };
