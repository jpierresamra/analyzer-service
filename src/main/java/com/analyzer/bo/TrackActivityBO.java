package com.analyzer.bo;

import common.obj.vo.analyzer.TrackActivityGridVO;
import common.obj.vo.common.GridParamsVO;
import common.obj.vo.exceptions.BOException;
import common.obj.vo.user.SecureUserVO;

public interface TrackActivityBO
{
	/*public String trackViewActivity(TrackActivityVO trackActivity) throws BOException;
	public void unTrackViewActivity(TrackActivityVO trackActivity) throws BOException;*/
	public TrackActivityGridVO getTrackActivityGrid(SecureUserVO sUser, GridParamsVO gridParamsVO)  throws BOException;
}
