/**
 * Copyright (C) 2015 Baifendian Corporation
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
 */
package com.bfd.harpc.loadbalance;

import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.Set;

import com.bfd.harpc.loadbalance.common.ResourceExhaustedException;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

/**
 * A load balancer that selects a random backend each time a request is made..
 */
public class RandomStrategy<S> extends StaticLoadBalancingStrategy<S> {

    private List<S> targets = Lists.newArrayList();

    private final Random random;

    public RandomStrategy() {
        this(new Random());
    }

    @VisibleForTesting
    RandomStrategy(Random random) {
        this.random = Preconditions.checkNotNull(random);
    }

    @Override
    protected Collection<S> onBackendsOffered(Set<S> targets) {
        this.targets = ImmutableList.copyOf(targets);
        return this.targets;
    }

    @Override
    public S nextBackend() throws ResourceExhaustedException {
        if (targets.isEmpty())
            throw new ResourceExhaustedException("No backends.");
        return targets.get(random.nextInt(targets.size()));
    }
}
