/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/

package titan.ccp.kiekerbridge.stages;

import java.util.function.Function;

import teetime.stage.basic.AbstractTransformation;

public class MapStage<I, O> extends AbstractTransformation<I, O> {

	private final Function<? super I, ? extends O> function;

	public MapStage(final Function<? super I, ? extends O> function) {
		super();
		this.function = function;
	}

	@Override
	protected void execute(final I element) {
		final O mappedElement = this.function.apply(element);
		this.getOutputPort().send(mappedElement);
	}

}
