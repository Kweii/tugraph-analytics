/*
 * Copyright 2023 AntGroup CO., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 */

package com.antgroup.geaflow.operator.impl.graph.compute.dynamic;

import com.antgroup.geaflow.api.graph.compute.IncVertexCentricAggCompute;
import com.antgroup.geaflow.api.graph.function.aggregate.VertexCentricAggContextFunction;
import com.antgroup.geaflow.api.graph.function.vc.IncVertexCentricAggComputeFunction;
import com.antgroup.geaflow.api.graph.traversal.VertexCentricAggTraversal;
import com.antgroup.geaflow.operator.impl.graph.algo.vc.GraphVertexCentricOpAggregator;
import com.antgroup.geaflow.operator.impl.graph.algo.vc.IGraphVertexCentricAggOp;
import com.antgroup.geaflow.view.graph.GraphViewDesc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DynamicGraphVertexCentricComputeWithAggOp<K, VV, EV, M, R, I, PA, PR, GA, GR>
    extends DynamicGraphVertexCentricComputeOp<K, VV, EV, M, IncVertexCentricAggComputeFunction<K, VV, EV, M, I, GR>>
    implements IGraphVertexCentricAggOp<K, VV, EV, M, I, PA, PR, GR> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicGraphVertexCentricComputeWithAggOp.class);

    private GraphVertexCentricOpAggregator<K, VV, EV, M, I, ?, ?, ?, GR,
            VertexCentricAggTraversal<K, VV, EV, M, R, I, ?, ?, ?, GR>> aggregator;

    public DynamicGraphVertexCentricComputeWithAggOp(
        GraphViewDesc graphViewDesc,
        IncVertexCentricAggCompute<K, VV, EV, M, I, PA, PR, GA, GR> vcAlgorithm) {
        super(graphViewDesc, vcAlgorithm);
        aggregator = new GraphVertexCentricOpAggregator(this);
    }

    @Override
    public void open(OpContext opContext) {
        super.open(opContext);
        aggregator.open((VertexCentricAggContextFunction) incVCComputeFunction);
    }

    @Override
    public void initIteration(long iteration) {
        super.initIteration(iteration);
        aggregator.initIteration(iteration);
    }

    public void finishIteration(long iteration) {
        super.finishIteration(iteration);
        aggregator.finishIteration(iteration);
    }

    @Override
    public void processAggregateResult(GR result) {
        aggregator.processAggregateResult(result);
    }

}
